# LanguIM external DSL for ArduinoML

LanguIM is a modern, readable DSL for defining Arduino-based state machines and hardware interactions. This folder contains a LanguIM implementation of the ArduinoML domain model, providing equivalent capabilities to the textX and ANTLR implementations.

## Features

LanguIM supports the complete ArduinoML feature set:

- **Hardware Bricks**: Digital sensors, actuators, buzzers, LCD displays, and analog potentiometers
- **State Machines**: Multiple states with entry actions and complex transitions
- **Actions**: Digital output control, LCD messages, and buzzer sounds
- **Conditions**: Boolean logic with AND/OR/NOT operators, sensor level checking, push event detection, and analog thresholds
- **Temporal Transitions**: Time-based state changes with millisecond or second precision
- **Validation**: Comprehensive model validation including pin constraints and LCD capacity checks

## Files

- `arduinoml.lim` — Grammar specification for the LanguIM DSL
- `languim_parser.py` — Lexer and parser that converts LanguIM files to an internal AST
- `languim_codegen.py` — Code generator that validates models and renders Arduino wiring code
- `main.py` — CLI tool for parsing models and generating code
- `examples/` — Sample LanguIM models demonstrating language features

## Quickstart

```bash
python main.py examples/toggle.lim
```

## Language Guide

### Application Declaration

```languim
app my_application
```

### Brick Declarations

Define hardware components:

```languim
brick button : digital_sensor pin 9
brick led : digital_actuator pin 12
brick bz : buzzer pin 8
brick pot : analog_input pin 0
brick screen : lcd 16x2
```

Supported brick types:
- `digital_sensor pin N` — Digital input sensor on pin N
- `digital_actuator pin N` — Digital output (LED, relay, etc.) on pin N
- `buzzer pin N` — Buzzer on pin N
- `analog_input pin N` — Analog sensor on pin N (0-5 for A0-A5)
- `lcd COLS x ROWS` — LCD display with COLS columns and ROWS rows

### Pin Constraints

- **Digital pins**: Must be 8, 9, 10, 11, or 12 (reserved pins avoid conflicts)
- **Analog pins**: Must be 0-5 (corresponding to A0-A5)
- **LCD messages**: Cannot exceed display capacity (cols × rows characters)

### State Definitions

States represent system modes. Exactly one state must be marked `initial`:

```languim
state idle {
    set led to LOW
    when button == HIGH then active
} initial

state active {
    set led to HIGH
    after 500 ms then idle
}
```

### Actions

Three types of actions execute when entering a state:

#### Set Action (Digital Output)
```languim
set actuator_name to HIGH
set actuator_name to LOW
```

#### Buzzer Action
```languim
beep(short) buzzer_name    // Short beep
beep(long) buzzer_name     // Long beep
beep(sound) buzzer_name    // Sound/melody
```

#### LCD Action
```languim
display "Hello World!" on screen_name
```

### Transitions

#### Conditional Transitions

Transition based on sensor conditions:

```languim
when sensor == HIGH then next_state
when button pressed then next_state
when pot >= 512 then next_state
```

Complex conditions with boolean logic:

```languim
when button == HIGH and pot < 256 then state1
when b1 == LOW or b2 == LOW then state2
when not button pressed then state3
```

#### Temporal Transitions

Automatic transitions after a delay:

```languim
after 500 ms then next_state
after 3 s then next_state
```

### Conditions

#### Sensor Level Condition
```languim
sensor == HIGH
sensor == LOW
```

#### Push Event Condition
```languim
button pressed
```

#### Analog Threshold Condition
```languim
potentiometer >= 512
potentiometer < 256
```

#### Boolean Operators
```languim
cond1 and cond2 and cond3    // All must be true
cond1 or cond2 or cond3      // At least one must be true
not condition                 // Negation
```

## Examples

### Simple Toggle Switch
```languim
app toggle

brick button : digital_sensor pin 9
brick led : digital_actuator pin 12

state off {
    set led to LOW
    when button == HIGH then on
} initial

state on {
    set led to HIGH
    when button == HIGH then off
}
```

### Alarm with Buzzer
```languim
app alarm

brick button : digital_sensor pin 9
brick led : digital_actuator pin 12
brick bz : buzzer pin 8

state idle {
    set led to LOW
    when button == HIGH then alarm
} initial

state alarm {
    set led to HIGH
    beep(short) bz
    when button == LOW then idle
}
```

### Multi-State Machine
```languim
app multi_state

brick button : digital_sensor pin 9
brick led : digital_actuator pin 11
brick bz : buzzer pin 10

state quiet {
    set led to LOW
    when button == HIGH then buzz
} initial

state buzz {
    beep(short) bz
    when button == HIGH then light
}

state light {
    set led to HIGH
    when button == HIGH then quiet
}
```

## Validation Rules

The parser enforces several validation rules:

1. **Unique Names**: All bricks and states must have unique names
2. **Valid References**: All referenced bricks/states must be declared
3. **Type Safety**: Actuators in `set` actions, sensors in conditions, buzzers for beeps, LCD for display
4. **Exactly One Initial State**: Required for deterministic startup
5. **Pin Constraints**: Digital pins must be in the allowed range, analog pins 0-5
6. **LCD Capacity**: Messages cannot exceed cols × rows characters

## Python API

The LanguIM implementation provides a Python API for programmatic use:

```python
from languim_parser import parse_languim
from languim_codegen import generate_wiring_code

# Parse a LanguIM file
model = parse_languim("examples/toggle.lim")

# Validate and generate Arduino code
code = generate_wiring_code(model)
print(code)
```

## Comparison with Other Implementations

| Feature | LanguIM | textX | ANTLR |
|---------|---------|-------|-------|
| Format | Readable DSL | textX Grammar | ANTLR4 Grammar |
| Implementation Language | Python | Python | Java |
| Parser Type | Custom | textX Framework | Generated Parser |
| Code Generation | Python Functions | textX/Jinja2 | Java Classes |
| Validation | Built-in | Custom | Custom |
| Extensibility | Direct Python | textX Metamodel | ANTLR Listener |

## Notes

- Comments are supported with `//` syntax
- Whitespace and newlines are flexible
- State order in the file does not matter; transitions find states by name
- Temporal transitions require explicit time units (ms or s)
- The generated Arduino code uses standard wiring conventions (digitalWrite, digitalRead, analogRead)
