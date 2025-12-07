"""
LanguIM Code Generator
Converts LanguIM models to Arduino wiring code
"""

from languim_parser import (
    ArduinoMLModel,
    State,
    SetAction,
    LCDAction,
    BeepAction,
    ConditionalTransition,
    TemporalTransition,
    SensorLevel,
    PushEvent,
    AnalogThreshold,
    OrCondition,
    AndCondition,
    NotCondition,
)


def validate_model(model: ArduinoMLModel):
    """Validate the ArduinoML model"""
    brick_names = set()
    brick_types = {}

    # Validate bricks
    for brick in model.bricks:
        if brick.name in brick_names:
            raise ValueError(f"Duplicate brick name: {brick.name}")
        brick_names.add(brick.name)
        brick_types[brick.name] = brick.brick_type

        if brick.brick_type == "potentiometer":
            if brick.pin is None or brick.pin < 0 or brick.pin > 5:
                raise ValueError(f"Illegal analog pin A{brick.pin} for {brick.name}")
            continue

        if brick.brick_type == "lcd":
            continue

        # Digital pins: 8, 9, 10, 11, 12 (avoiding reserved pins for LCD)
        allowed_pins = {8, 9, 10, 11, 12}
        if brick.pin is None or brick.pin not in allowed_pins:
            raise ValueError(
                f"Illegal pin {brick.pin} for {brick.name} (allowed: {sorted(allowed_pins)})"
            )

    # Validate states
    state_names = set()
    initial_count = 0

    for state in model.states:
        if state.name in state_names:
            raise ValueError(f"Duplicate state name: {state.name}")
        state_names.add(state.name)

        if state.initial:
            initial_count += 1

        # Validate actions
        for action in state.actions:
            if isinstance(action, SetAction):
                if action.target not in brick_names:
                    raise ValueError(f"Unknown brick: {action.target}")
                if brick_types.get(action.target) != "actuator":
                    raise ValueError(f"{action.target} is not an actuator")
            elif isinstance(action, LCDAction):
                if action.screen not in brick_names:
                    raise ValueError(f"Unknown brick: {action.screen}")
                if brick_types.get(action.screen) != "lcd":
                    raise ValueError(f"{action.screen} is not an LCD")
                # Validate message length
                lcd_brick = next(b for b in model.bricks if b.name == action.screen)
                capacity = lcd_brick.cols * lcd_brick.rows
                if len(action.message) > capacity:
                    raise ValueError(
                        f"LCD message too long for {action.screen}: {len(action.message)}/{capacity}"
                    )
            elif isinstance(action, BeepAction):
                if action.target not in brick_names:
                    raise ValueError(f"Unknown brick: {action.target}")
                if brick_types.get(action.target) != "buzzer":
                    raise ValueError(f"{action.target} is not a buzzer")

        # Validate transitions
        for transition in state.transitions:
            if isinstance(transition, ConditionalTransition):
                if transition.next_state not in state_names:
                    raise ValueError(f"Unknown state: {transition.next_state}")
                _validate_condition(transition.condition, brick_names, brick_types)
            elif isinstance(transition, TemporalTransition):
                if transition.next_state not in state_names:
                    raise ValueError(f"Unknown state: {transition.next_state}")

    if initial_count != 1:
        raise ValueError("Exactly one initial state is required")


def _validate_condition(condition, brick_names, brick_types):
    """Recursively validate condition references"""
    if isinstance(condition, SensorLevel):
        if condition.sensor not in brick_names:
            raise ValueError(f"Unknown brick: {condition.sensor}")
        if brick_types.get(condition.sensor) not in ["sensor", "actuator"]:
            raise ValueError(f"{condition.sensor} cannot be used in conditions")
    elif isinstance(condition, PushEvent):
        if condition.sensor not in brick_names:
            raise ValueError(f"Unknown brick: {condition.sensor}")
        if brick_types.get(condition.sensor) != "sensor":
            raise ValueError(f"{condition.sensor} is not a sensor")
    elif isinstance(condition, AnalogThreshold):
        if condition.sensor not in brick_names:
            raise ValueError(f"Unknown brick: {condition.sensor}")
        if brick_types.get(condition.sensor) != "potentiometer":
            raise ValueError(f"{condition.sensor} is not a potentiometer")
    elif isinstance(condition, (OrCondition, AndCondition)):
        for operand in condition.operands:
            _validate_condition(operand, brick_names, brick_types)
    elif isinstance(condition, NotCondition):
        _validate_condition(condition.operand, brick_names, brick_types)


def generate_wiring_code(model: ArduinoMLModel) -> str:
    """Generate Arduino wiring code from the model"""
    validate_model(model)

    code = []
    code.append("// Generated Arduino code from LanguIM model")
    code.append(f"// Application: {model.app_name}")
    code.append("")

    # Pin definitions
    code.append("// Pin definitions")
    for brick in model.bricks:
        if brick.brick_type == "sensor":
            code.append(f"#define PIN_{brick.name.upper()} {brick.pin}")
        elif brick.brick_type == "actuator":
            code.append(f"#define PIN_{brick.name.upper()} {brick.pin}")
        elif brick.brick_type == "buzzer":
            code.append(f"#define PIN_{brick.name.upper()} {brick.pin}")
        elif brick.brick_type == "potentiometer":
            code.append(f"#define PIN_{brick.name.upper()} {brick.pin}")

    code.append("")

    # State enumeration
    code.append("// State enumeration")
    code.append("enum State {")
    for state in model.states:
        code.append(f"  STATE_{state.name.upper()},")
    code.append("};")
    code.append("")

    # Initial state
    initial_state = next(s for s in model.states if s.initial)
    code.append("// Current state")
    code.append(f"State current_state = STATE_{initial_state.name.upper()};")
    code.append("")

    # Setup function
    code.append("void setup() {")
    code.append("  Serial.begin(9600);")
    for brick in model.bricks:
        if brick.brick_type in ["actuator", "sensor", "buzzer"]:
            if brick.brick_type == "sensor":
                code.append(f"  pinMode(PIN_{brick.name.upper()}, INPUT);")
            else:
                code.append(f"  pinMode(PIN_{brick.name.upper()}, OUTPUT);")

    code.append("}")
    code.append("")

    # Main loop and state handlers
    code.append("void loop() {")
    code.append("  switch (current_state) {")

    for state in model.states:
        code.append(f"    case STATE_{state.name.upper()}:")
        code.append("      {")

        # Actions
        for action in state.actions:
            code.extend(_generate_action_code(action))

        # Transitions
        for transition in state.transitions:
            code.extend(_generate_transition_code(transition))

        code.append("      }")
        code.append("      break;")

    code.append("  }")
    code.append("}")
    code.append("")

    return "\n".join(code)


def _generate_action_code(action) -> list:
    """Generate code for an action"""
    code = []
    if isinstance(action, SetAction):
        value = "HIGH" if action.value.value == "HIGH" else "LOW"
        code.append(f"        digitalWrite(PIN_{action.target.upper()}, {value});")
    elif isinstance(action, LCDAction):
        code.append(
            f"        // Display '{action.message}' on {action.screen}"
        )
    elif isinstance(action, BeepAction):
        code.append(f"        // Beep action: {action.kind} on {action.target}")
    return code


def _generate_transition_code(transition) -> list:
    """Generate code for a transition"""
    code = []
    if isinstance(transition, ConditionalTransition):
        condition_code = _generate_condition_code(transition.condition)
        code.append(f"        if ({condition_code}) {{")
        code.append(f"          current_state = STATE_{transition.next_state.upper()};")
        code.append("        }")
    elif isinstance(transition, TemporalTransition):
        duration_ms = (
            transition.duration if transition.unit == "ms" else transition.duration * 1000
        )
        code.append(f"        // After {duration_ms} ms transition to {transition.next_state}")
    return code


def _generate_condition_code(condition) -> str:
    """Generate code for a condition"""
    if isinstance(condition, SensorLevel):
        value = "HIGH" if condition.value.value == "HIGH" else "LOW"
        return f"digitalRead(PIN_{condition.sensor.upper()}) == {value}"
    elif isinstance(condition, PushEvent):
        return f"digitalRead(PIN_{condition.sensor.upper()}) == HIGH"
    elif isinstance(condition, AnalogThreshold):
        comp = condition.comp
        return f"analogRead(PIN_{condition.sensor.upper()}) {comp} {condition.threshold}"
    elif isinstance(condition, OrCondition):
        operands = [_generate_condition_code(op) for op in condition.operands]
        return " || ".join(f"({op})" for op in operands)
    elif isinstance(condition, AndCondition):
        operands = [_generate_condition_code(op) for op in condition.operands]
        return " && ".join(f"({op})" for op in operands)
    elif isinstance(condition, NotCondition):
        operand_code = _generate_condition_code(condition.operand)
        return f"!({operand_code})"
    else:
        raise ValueError(f"Unknown condition type: {type(condition)}")
