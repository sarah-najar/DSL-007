# textX external DSL for ArduinoML

This folder mirrors the ArduinoML domain model (bricks, states with entry actions, conditional and temporal transitions, LCD and buzzer support, analog thresholds) using a textX grammar.

## Overview

textX is a metamodel-driven language engineering framework that enables rapid development of domain-specific languages. This implementation demonstrates textX's approach to DSL development, with metamodel-based parsing and Jinja2 template-based code generation.

## Comparison with Other Implementations

This folder is part of a larger project with three external DSL implementations:

| Implementation | Approach | Technology | Recommended For |
|---|---|---|---|
| [LanguIM](../languim/) | Custom Parser | Python | Quick prototyping, teaching |
| **textX** | Metamodel-driven | textX Framework | DSL engineering, ecosystem use |
| [ANTLR4](../antlr/) | Formal Grammar | ANTLR4 + Java | Large projects, formal specs |

For a comparison of all implementations, see [externals/README.md](../README.md).

## Files
- `arduinoml.tx` — grammar for the DSL.
- `wiring_gen.py` — loads a model, validates it, and renders Wiring code with debounce guards, temporal transitions, buzzer/LCD handling, and analog comparisons.
- `main.py` — tiny CLI that parses a model file and prints the generated sketch.
- `examples/` — sample models (`toggle.aml`).

## Quickstart
```
pip install textX
python main.py examples/toggle.aml
```

## Notes
- Pins: digital must be 1..9, 11, 12; analog inputs are 0..5 (A0..A5).
- Exactly one state must be marked `initial`.
- LCD messages are checked against the screen capacity (`cols * rows`).

## textX Framework Benefits

- **Metamodel-driven**: Define your language structure once, get parser and validation
- **Extensible**: Hooks for custom object instantiation and validation
- **Error handling**: Detailed error messages with line/column information
- **Integration**: Works with textX-based tools and IDEs
- **Documentation**: Well-established patterns for DSL development
