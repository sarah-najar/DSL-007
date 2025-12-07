# textX External DSL — Implementation Notes and Validation

## What’s implemented
- Grammar (`textx/arduinoml.tx`) mirroring ArduinoML: application header, bricks (sensor, actuator, buzzer, LCD, potentiometer), states with entry actions, conditional and temporal transitions, boolean logic (and/or/not), push events, analog thresholds, buzzer and LCD actions.
- Generator (`textx/wiring_gen.py`) producing Wiring code with:
  - Debounce guards per sensor and state-based dispatch.
  - BUS-aware pin validation: without LCD → pins 8–12; with LCD BUS1 mapping → LCD owns 2–8, leaves 9–12 for other bricks.
  - LCD support on BUS1 (rs=2, rw=3, en=4, d4=5, d5=6, d6=7, d7=8), RW driven low in setup, padded prints to avoid flicker.
  - Buzzer short/long pulses, analog read for potentiometers.
- Examples in `textx/examples/` cover scenarios:
  - Basic toggle (`toggle.aml`), simple/dual alarms, multi-state alarm, temporal after push, LCD status, pot threshold.
  - Extensions exercised: temporal transitions (`temporal_after_push.aml`), LCD output (`lcd_status.aml`), multi-state cycling (`multi_state_alarm.aml`), composite conditions.

## Validation and tests
- Parsing + codegen run locally via `python textx/main.py textx/examples/*.aml` (all succeed).
- Hardware checks on Arduino Uno-compatible board:
  - `lcd_status.aml` deployed with BUS1 LCD cable, button on D9, LED on D10 → stable “READY” / “ALARM ON” display, correct LED behavior.
  - `multi_state_alarm.aml` deployed with button D9, buzzer D10, LED D11 → cycles buzz/light/quiet on successive presses.
  - `toggle.aml` deployed with button D9, LED D12, buzzer D8 → toggles LED/buzzer on press with debounce.

## Known limitations
- No comment syntax in `.aml` files (grammar rejects comments).
- LCD mapping is fixed to BUS1 (rs=2, rw=3, en=4, d4=5, d5=6, d6=7, d7=8); BUS2 or I²C LCDs are not supported in the generator.
- Pin validation is simplified (digital pins limited to 8–12 without LCD, 9–12 with LCD); analog inputs only for potentiometers (A0–A5 via indices 0–5).
- LCD actions write to line 0 only; no cursor control for multiple lines.
- No model-level validation beyond uniqueness, pin range, single initial state, and LCD message length.
