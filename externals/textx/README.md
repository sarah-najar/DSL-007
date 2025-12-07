# textX external DSL for ArduinoML

This folder mirrors the ArduinoML domain model (bricks, states with entry actions, conditional and temporal transitions, LCD and buzzer support, analog thresholds) using a textX grammar.

## Files
- `arduinoml.tx` — grammar for the DSL.
- `wiring_gen.py` — loads a model, validates it, and renders Wiring code with debounce guards, temporal transitions, buzzer/LCD handling, and analog comparisons.
- `main.py` — tiny CLI that parses a model file and prints the generated sketch.
- `examples/` — sample models (`toggle.aml`).

## Quickstart
```
pip install textX
python textx/main.py textx/examples/toggle.aml
```

## Notes
- Pins: digital must be 1..9, 11, 12; analog inputs are 0..5 (A0..A5).
- Exactly one state must be marked `initial`.
- LCD messages are checked against the screen capacity (`cols * rows`).
