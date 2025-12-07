"""
LanguIM Parser for ArduinoML
Converts LanguIM DSL files to an internal AST model
"""

import re
from dataclasses import dataclass, field
from typing import List, Optional, Dict, Set


@dataclass
class Signal:
    value: str  # HIGH or LOW

    def __repr__(self):
        return f"Signal({self.value})"


@dataclass
class Brick:
    name: str
    brick_type: str
    pin: Optional[int] = None
    cols: Optional[int] = None
    rows: Optional[int] = None

    def __repr__(self):
        if self.brick_type in ["lcd"]:
            return f"Brick({self.name}, {self.brick_type}, {self.cols}x{self.rows})"
        return f"Brick({self.name}, {self.brick_type}, pin={self.pin})"


@dataclass
class SetAction:
    target: str
    value: Signal

    def __repr__(self):
        return f"SetAction({self.target}, {self.value})"


@dataclass
class LCDAction:
    message: str
    screen: str

    def __repr__(self):
        return f"LCDAction({self.message}, {self.screen})"


@dataclass
class BeepAction:
    kind: str  # short, long, sound
    target: str

    def __repr__(self):
        return f"BeepAction({self.kind}, {self.target})"


Action = SetAction | LCDAction | BeepAction


@dataclass
class SensorLevel:
    sensor: str
    value: Signal

    def __repr__(self):
        return f"SensorLevel({self.sensor} == {self.value})"


@dataclass
class PushEvent:
    sensor: str

    def __repr__(self):
        return f"PushEvent({self.sensor} pressed)"


@dataclass
class AnalogThreshold:
    sensor: str
    comp: str  # >= or <
    threshold: int

    def __repr__(self):
        return f"AnalogThreshold({self.sensor} {self.comp} {self.threshold})"


@dataclass
class OrCondition:
    operands: List

    def __repr__(self):
        return f"Or({' or '.join(str(o) for o in self.operands)})"


@dataclass
class AndCondition:
    operands: List

    def __repr__(self):
        return f"And({' and '.join(str(o) for o in self.operands)})"


@dataclass
class NotCondition:
    operand: object

    def __repr__(self):
        return f"Not({self.operand})"


Condition = SensorLevel | PushEvent | AnalogThreshold | OrCondition | AndCondition | NotCondition


@dataclass
class ConditionalTransition:
    condition: Condition
    next_state: str

    def __repr__(self):
        return f"Transition(when {self.condition} then {self.next_state})"


@dataclass
class TemporalTransition:
    duration: int
    unit: str  # ms or s
    next_state: str

    def __repr__(self):
        return f"Transition(after {self.duration} {self.unit} then {self.next_state})"


Transition = ConditionalTransition | TemporalTransition


@dataclass
class State:
    name: str
    actions: List[Action] = field(default_factory=list)
    transitions: List[Transition] = field(default_factory=list)
    initial: bool = False

    def __repr__(self):
        return f"State({self.name}, initial={self.initial})"


@dataclass
class ArduinoMLModel:
    app_name: str
    bricks: List[Brick] = field(default_factory=list)
    states: List[State] = field(default_factory=list)

    def __repr__(self):
        return f"App({self.app_name})"


class LanguIMParser:
    def __init__(self, text: str):
        self.text = text
        self.lines = [line.strip() for line in text.split("\n")]
        self.lines = [line for line in self.lines if line and not line.startswith("//")]
        self.pos = 0
        self.current_line_idx = 0

    def parse(self) -> ArduinoMLModel:
        model = ArduinoMLModel("")

        # Parse application declaration
        if self.current_line_idx < len(self.lines):
            line = self.lines[self.current_line_idx]
            if line.startswith("app "):
                model.app_name = line[4:].strip()
                self.current_line_idx += 1

        # Parse bricks
        while self.current_line_idx < len(self.lines):
            line = self.lines[self.current_line_idx]
            if line.startswith("brick "):
                model.bricks.append(self._parse_brick(line))
                self.current_line_idx += 1
            elif line.startswith("state "):
                break
            else:
                self.current_line_idx += 1

        # Parse states
        while self.current_line_idx < len(self.lines):
            line = self.lines[self.current_line_idx]
            if line.startswith("state "):
                model.states.append(self._parse_state())
            else:
                self.current_line_idx += 1

        return model

    def _parse_brick(self, line: str) -> Brick:
        # Format: brick name : type
        match = re.match(r"brick\s+(\w+)\s*:\s*(.*)", line)
        if not match:
            raise ValueError(f"Invalid brick declaration: {line}")

        name = match.group(1)
        rest = match.group(2).strip()

        if "digital_sensor" in rest:
            pin = self._extract_pin(rest)
            return Brick(name, "sensor", pin)
        elif "digital_actuator" in rest:
            pin = self._extract_pin(rest)
            return Brick(name, "actuator", pin)
        elif "buzzer" in rest:
            pin = self._extract_pin(rest)
            return Brick(name, "buzzer", pin)
        elif "analog_input" in rest:
            pin = self._extract_pin(rest)
            return Brick(name, "potentiometer", pin)
        elif "lcd" in rest:
            cols, rows = self._extract_lcd_dims(rest)
            return Brick(name, "lcd", cols=cols, rows=rows)
        else:
            raise ValueError(f"Unknown brick type in: {line}")

    def _extract_pin(self, text: str) -> int:
        match = re.search(r"pin\s+(\d+)", text)
        if match:
            return int(match.group(1))
        raise ValueError(f"Could not extract pin from: {text}")

    def _extract_lcd_dims(self, text: str) -> tuple:
        match = re.search(r"(\d+)\s*x\s*(\d+)", text)
        if match:
            return int(match.group(1)), int(match.group(2))
        raise ValueError(f"Could not extract LCD dimensions from: {text}")

    def _parse_state(self) -> State:
        line = self.lines[self.current_line_idx]
        self.current_line_idx += 1

        # Extract state name and check for initial
        match = re.match(r"state\s+(\w+)", line)
        if not match:
            raise ValueError(f"Invalid state declaration: {line}")

        state_name = match.group(1)
        state = State(state_name)

        # Parse state body
        while self.current_line_idx < len(self.lines):
            line = self.lines[self.current_line_idx]

            if line == "}":
                self.current_line_idx += 1
                # Check for initial keyword on next line or same position
                if self.current_line_idx < len(self.lines):
                    next_line = self.lines[self.current_line_idx]
                    if next_line == "initial":
                        state.initial = True
                        self.current_line_idx += 1
                break
            elif line == "} initial":
                state.initial = True
                self.current_line_idx += 1
                break
            elif line == "{":
                self.current_line_idx += 1
                continue
            elif line.startswith("set "):
                state.actions.append(self._parse_set_action(line))
            elif line.startswith("display "):
                state.actions.append(self._parse_lcd_action(line))
            elif line.startswith("beep"):
                state.actions.append(self._parse_beep_action(line))
            elif line.startswith("when "):
                state.transitions.append(self._parse_conditional_transition(line))
            elif line.startswith("after "):
                state.transitions.append(self._parse_temporal_transition(line))

            self.current_line_idx += 1

        return state

    def _parse_set_action(self, line: str) -> SetAction:
        # Format: set target to value
        match = re.match(r"set\s+(\w+)\s+to\s+(HIGH|LOW)", line)
        if not match:
            raise ValueError(f"Invalid set action: {line}")
        target = match.group(1)
        value = Signal(match.group(2))
        return SetAction(target, value)

    def _parse_lcd_action(self, line: str) -> LCDAction:
        # Format: display "message" on screen
        match = re.match(r'display\s+"([^"]*)"\s+on\s+(\w+)', line)
        if not match:
            raise ValueError(f"Invalid LCD action: {line}")
        message = match.group(1)
        screen = match.group(2)
        return LCDAction(message, screen)

    def _parse_beep_action(self, line: str) -> BeepAction:
        # Format: beep(kind) target
        match = re.match(r"beep\((\w+)\)\s+(\w+)", line)
        if not match:
            raise ValueError(f"Invalid beep action: {line}")
        kind = match.group(1)
        target = match.group(2)
        return BeepAction(kind, target)

    def _parse_conditional_transition(self, line: str) -> ConditionalTransition:
        # Format: when condition then state
        match = re.match(r"when\s+(.*?)\s+then\s+(\w+)", line)
        if not match:
            raise ValueError(f"Invalid conditional transition: {line}")
        condition_str = match.group(1)
        next_state = match.group(2)
        condition = self._parse_condition(condition_str)
        return ConditionalTransition(condition, next_state)

    def _parse_temporal_transition(self, line: str) -> TemporalTransition:
        # Format: after duration unit then state
        match = re.match(r"after\s+(\d+)\s+(ms|s)\s+then\s+(\w+)", line)
        if not match:
            raise ValueError(f"Invalid temporal transition: {line}")
        duration = int(match.group(1))
        unit = match.group(2)
        next_state = match.group(3)
        return TemporalTransition(duration, unit, next_state)

    def _parse_condition(self, cond_str: str) -> Condition:
        # Handle OR at top level
        if " or " in cond_str:
            parts = cond_str.split(" or ")
            operands = [self._parse_condition(p.strip()) for p in parts]
            return OrCondition(operands)

        # Handle AND
        if " and " in cond_str:
            parts = cond_str.split(" and ")
            operands = [self._parse_condition(p.strip()) for p in parts]
            return AndCondition(operands)

        # Handle NOT
        if cond_str.startswith("not "):
            operand = self._parse_condition(cond_str[4:].strip())
            return NotCondition(operand)

        # Handle primary conditions
        # Sensor level: sensor == HIGH/LOW
        if "==" in cond_str:
            match = re.match(r"(\w+)\s*==\s*(HIGH|LOW)", cond_str)
            if match:
                return SensorLevel(match.group(1), Signal(match.group(2)))

        # Push event: sensor pressed
        if "pressed" in cond_str:
            match = re.match(r"(\w+)\s+pressed", cond_str)
            if match:
                return PushEvent(match.group(1))

        # Analog threshold: sensor >= threshold or sensor < threshold
        match = re.match(r"(\w+)\s*(>=|<)\s*(\d+)", cond_str)
        if match:
            return AnalogThreshold(match.group(1), match.group(2), int(match.group(3)))

        raise ValueError(f"Could not parse condition: {cond_str}")


def parse_languim(filename: str) -> ArduinoMLModel:
    with open(filename, "r") as f:
        text = f.read()
    parser = LanguIMParser(text)
    return parser.parse()
