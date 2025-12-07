# DSL-007

développement d'un langage spécifique (DSL) basé sur ArduinoML.

## Overview

This project explores the development of domain-specific languages (DSLs) for Arduino programming through the ArduinoML case study. The project demonstrates three different approaches to implementing the same DSL using different technologies and methodologies.

## Project Structure

```
DSL-007/
├── embeddeds/              # Host language implementations
│   ├── groovy/             # Groovy-based fluent API
│   ├── java/               # Pure Java implementation
│   └── python/             # Python-based fluent API
├── externals/              # External DSL implementations
│   ├── languim/            # LanguIM: Modern readable DSL (Python-based)
│   ├── textx/              # textX: Metamodel-driven approach (Python)
│   └── antlr/              # ANTLR4: Formal grammar approach (Java)
├── kernels/                # Core domain models
│   ├── jvm/                # JVM kernel (Java)
│   └── uml/                # UML domain model
└── docs/                   # Documentation and reports
```

## External DSL Implementations

The `externals/` folder contains three distinct implementations of the ArduinoML DSL:

### LanguIM (Recommended for rapid prototyping)

A modern, highly readable DSL with a custom Python parser. Perfect for quick prototyping and teaching DSL concepts.

**Features:**
- Clean, English-like syntax
- Comprehensive validation
- Direct Python API
- Fast parsing and code generation

**Quick Start:**
```bash
cd externals/languim
python main.py examples/toggle.lim
```

### textX (Metamodel-driven approach)

Uses the textX framework for metamodel-driven development, leveraging established DSL engineering patterns.

**Features:**
- Metamodel-based architecture
- Jinja2 template-based code generation
- textX ecosystem integration
- Validated abstract syntax trees

**Quick Start:**
```bash
cd externals/textx
pip install textX
python main.py examples/toggle.aml
```

### ANTLR4 (Formal grammar approach)

A production-grade implementation using ANTLR4 parser generator with formal grammar specifications.

**Features:**
- Formal ANTLR4 grammar
- Generated parser and lexer
- Java-based implementation
- Visitor pattern for AST traversal

**Quick Start:**
```bash
cd externals/antlr
mvn clean package
mvn exec:java -Dexec.args="src/main/resources/toggle.arduinoml"
```

## Embedded (Host Language) Implementations

The `embeddeds/` folder contains internal DSL implementations using host language features:

- **Groovy**: Fluent API exploiting Groovy's dynamic features and metaprogramming
- **Java**: Static fluent API for type-safe Arduino programming
- **Python**: Fluent API with Python's flexible function definitions

## Key Features Across All Implementations

All DSL implementations support:

- **Hardware Components**: Sensors, actuators, buzzers, LCD displays, analog inputs
- **State Machines**: Multiple states with entry actions
- **Complex Transitions**: Conditional and temporal transitions
- **Boolean Logic**: AND, OR, NOT operators in conditions
- **Validation**: Pin constraints, LCD capacity, state references
- **Code Generation**: Arduino wiring code output

## Example: Simple Toggle Switch

All three external DSL implementations can express this Arduino behavior:

**LanguIM:**
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

**textX:**
```
application toggle
sensor button:9
actuator led:12

state off {
    set led to LOW
    when button is HIGH goto on
} initial

state on {
    set led to HIGH
    when button is HIGH goto off
}
```

**ANTLR4:**
```
application toggle
sensor button : 9
actuator led : 12

state off {
    set led to HIGH
    when button is HIGH goto on
} initial

state on {
    set led to HIGH
    when button is HIGH goto off
}
```

## Documentation

- [External DSL Implementations](externals/README.md) - Comprehensive comparison of all three approaches
- [LanguIM Guide](externals/languim/README.md) - Detailed LanguIM documentation
- [textX Guide](externals/textx/README.md) - textX implementation details
- [ANTLR4 Guide](externals/antlr/README.md) - ANTLR4 implementation details
- [GroovuinoML Report](GroovuinoML_RAPPORT.md) - Original project documentation

## Objectives

This project demonstrates:

1. **DSL Design Patterns**: Syntax design and semantic validation
2. **Parser Technologies**: Custom parsers, metamodel frameworks, formal grammars
3. **Code Generation**: Template-based and direct code generation
4. **Language Implementation**: Complete DSL toolchain development
5. **Comparative Analysis**: Strengths and weaknesses of different approaches

## Technologies Used

- **Languages**: Python, Java, Groovy, ANTLR4, textX
- **Frameworks**: textX (metamodel engineering), ANTLR4 (parser generation), Maven (build)
- **Target**: Arduino wiring code generation

## Getting Started

1. Choose an implementation (LanguIM recommended for beginners)
2. Navigate to `externals/<implementation>/`
3. Read the README.md for detailed instructions
4. Run an example: `python main.py examples/toggle.lim` (LanguIM)
5. Create your own model and generate code!
