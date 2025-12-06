GroovuinoML (Groovy DSL for ArduinoML)

Overview
- This module provides a Groovy-embedded DSL that targets the updated ArduinoML kernel (kernels/jvm), including:
  - Digital sensors/actuators, analog sensors (potentiometer), buzzer patterns, LCD messages
  - Temporal transitions with units (ms, s, …)
  - Composite conditions (AND/OR) over digital and analog readings

Requirements
- JDK 8 or newer
- Maven 3+

Build
1) Build and install the kernel (so the Groovy module can depend on it):
   - `mvn -q -DskipTests -pl kernels/jvm -am install`
2) Build the Groovy DSL module and assemble a runnable fat jar:
   - `mvn -q -DskipTests -pl embeddeds/groovy/GroovuinoML -am package assembly:single`
   - The fat jar will be at:
     `embeddeds/groovy/GroovuinoML/target/dsl-groovy-1.0-jar-with-dependencies.jar`

Run a script
- General form:
  - `java -cp embeddeds/groovy/GroovuinoML/target/dsl-groovy-1.0-jar-with-dependencies.jar groovuinoml.main.GroovuinoML <script.groovy>`
- Scripts are provided in `embeddeds/groovy/GroovuinoML/scripts`.
  Examples:
  - `java -cp ... GroovuinoML embeddeds/groovy/GroovuinoML/scripts/Alarm.groovy`
  - `java -cp ... GroovuinoML embeddeds/groovy/GroovuinoML/scripts/TwoButtonsAnd.groovy`

DSL cheatsheet
- Bricks
  - Digital sensor: `sensor "b1" pin 2`
  - Digital actuator: `actuator "led" pin 12`
  - Analog sensor (potentiometer on A0..A5 as 0..5): `potentiometer "pot" on 0`  (A0 → 0)
  - Buzzer: `buzzer "bz" pin 8`
  - LCD: `lcd "screen" size 16, 2`
- States and actions
  - Define state and actions: `state "idle" means "led" becomes low`
  - Chain multiple actions: `state "on" means "led" becomes high and "bz" shortBeep`
  - Buzzer actions: `shortBeep`, `longBeep`
  - LCD action: `display "HELLO"`
  - Set initial: `initial "idle"`
- Transitions
  - Digital level: `from "idle" to "on" when "b1" becomes high` (also `low`)
  - Temporal:
    - Milliseconds: `from "on" to "idle" after 800`
    - With unit: `from "off" to "on" after 1, s` (available: `ms`, `s`, …)
  - Composite helpers:
    - `whenBothHigh "b1", "b2"`
    - `whenPotAtLeast "pot", 600`
    - `whenPotBelow "pot", 600`
    - `whenButtonAndPotAtLeast "b1", "pot", 512`
    - `whenEitherButtonLowOrPotBelow "b1", "pot", 512`
- Generate code
  - `export "appname"` prints the Wiring/INO program to stdout and can be redirected to a file.

Examples provided (scripts/)
- `Alarm.groovy` — button triggers LED+buzzer; back to idle after 500ms
- `TwoButtonsAnd.groovy` — two buttons in AND to light a LED
- `StateSwitch.groovy` — toggle LED with a single button
- `MultiState.groovy` — Quiet → Buzz → Light cycling on button presses
- `TimerBlink.groovy` — 500ms ON, 1s OFF blink
- `TemporalAfterPush.groovy` — press to turn ON, auto OFF after 800ms
- `Beeps.groovy` — alternates short and long beeps
- `LCD_OK.groovy` — display text on a 16x2 LCD
- `PotOnly.groovy` — LED controlled by potentiometer threshold
- `DualButtonAndPot.groovy` — button AND pot>=TH to alarm; else back

Notes
- Analog pins: use indices 0..5 to represent A0..A5 in `potentiometer "pot" on <index>`.
- Time units: bound to variables matching kernel `TimeUnit` enum names in lowercase, e.g., `ms`, `s`.
- Validation: model constraints (e.g., LCD message length) are enforced in the kernel validator/generator; invalid models will fail generation.

Troubleshooting
- If the fat jar isn’t created, ensure you ran `assembly:single` as shown above.
- If Maven needs network to download dependencies, make sure you have internet access or a pre-warmed local repository.
