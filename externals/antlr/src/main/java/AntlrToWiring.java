import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

/**
 * ANTLR-local Wiring generator with LCD support (BUS1/I2C 1602).
 * Leaves the shared kernel untouched by duplicating the generation logic here.
 */
public class AntlrToWiring extends Visitor<StringBuffer> {
    enum PASS {ONE, TWO}

    private boolean hasLcd = false;
    private App currentApp;
    private String lcdVarName = "lcd";
    private int lcdColumns = 16;
    private int lcdRows = 2;

    public AntlrToWiring() {
        this.result = new StringBuffer();
    }

    private void w(String s) {
        result.append(String.format("%s", s));
    }

    @Override
    public void visit(App app) {
        // detect components first
        hasLcd = false;
        for (Brick b : app.getBricks()) {
            if (b instanceof LCDScreen) {
                hasLcd = true;
            }
        }

        if (hasLcd) {
            w("#include <LiquidCrystal.h>\n");
            w("// LCD BUS1 mapping: rs=2, rw=3, en=4, d4=5, d5=6, d6=7, d7=8\n");
        }

        // first pass, create global vars
        context.put("pass", PASS.ONE);
        w("// Wiring code generated from an ArduinoML model (ANTLR path)\n");
        w(String.format("// Application name: %s\n", app.getName()) + "\n");

        this.currentApp = app;

        w("long debounce = 200;\n");
        w("\nenum STATE {");
        String sep = "";
        for (State state : app.getStates()) {
            w(sep);
            state.accept(this);
            sep = ", ";
        }
        w("};\n");
        if (app.getInitial() != null) {
            w("STATE currentState = " + app.getInitial().getName() + ";\n");
        }

        for (Brick brick : app.getBricks()) {
            brick.accept(this);
        }

        // second pass, setup and loop
        context.put("pass", PASS.TWO);
        w("\nvoid setup(){\n");
        for (Brick brick : app.getBricks()) {
            brick.accept(this);
        }
        w("}\n");

        w("\nvoid loop() {\n" +
                "\tswitch(currentState){\n");
        for (State state : app.getStates()) {
            state.accept(this);
        }
        w("\t}\n" +
                "}");
    }

    @Override
    public void visit(Actuator actuator) {
        if (context.get("pass") == PASS.ONE) {
            if (actuator instanceof LCDScreen) {
                LCDScreen lcd = (LCDScreen) actuator;
                lcdColumns = lcd.getColumns();
                lcdRows = lcd.getRows();
                w(String.format("LiquidCrystal %s(2, 3, 4, 5, 6, 7, 8);\n", lcdVarName));
            }
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            if (actuator instanceof LCDScreen) {
                w("  pinMode(3, OUTPUT); // RW\n");
                w("  digitalWrite(3, LOW); // tie RW to GND\n");
                w(String.format("  %s.begin(%d, %d);\n", lcdVarName, lcdColumns, lcdRows));
                w("  delay(50);\n");
                return;
            }
            // Only emit pinMode for bricks with a valid pin (e.g., not LCD screens without direct pin mapping)
            if (actuator.getPin() > 0) {
                w(String.format("  pinMode(%d, OUTPUT); // %s [Actuator]\n", actuator.getPin(), actuator.getName()));
            } else {
                w(String.format("  // %s [Actuator] (no pin setup required)\n", actuator.getName()));
            }
            return;
        }
    }

    @Override
    public void visit(Sensor sensor) {
        if (context.get("pass") == PASS.ONE) {
            w(String.format("\nboolean %sBounceGuard = false;\n", sensor.getName()));
            w(String.format("long %sLastDebounceTime = 0;\n", sensor.getName()));
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            // For analog sensors, no explicit pinMode is required for analogRead.
            if (sensor instanceof io.github.mosser.arduinoml.kernel.structural.AnalogSensor) {
                w(String.format("  // %s [AnalogSensor] (analogRead on A%d)\n", sensor.getName(), sensor.getPin()));
            } else {
                w(String.format("  pinMode(%d, INPUT);  // %s [Sensor]\n", sensor.getPin(), sensor.getName()));
            }
            return;
        }
    }

    @Override
    public void visit(State state) {
        if (context.get("pass") == PASS.ONE) {
            w(state.getName());
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            w("\t\tcase " + state.getName() + ":\n");
            // Compute debounce guards for all sensors once per state tick
            for (Brick b : currentApp.getBricks()) {
                if (b instanceof Sensor) {
                    String sensorName = b.getName();
                    w(String.format("\t\t\t%sBounceGuard = millis() - %sLastDebounceTime > debounce;\n", sensorName, sensorName));
                }
            }
            for (Action action : state.getActions()) {
                action.accept(this);
            }

            for (Transition t : state.getTransitions()) {
                t.accept(this);
            }
            w("\t\tbreak;\n");
            return;
        }

    }

    @Override
    public void visit(ConditionalTransition transition) {
        if (context.get("pass") == PASS.ONE) {
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            String expr = conditionToExpr(transition.getCondition());
            w(String.format("\t\t\tif( %s ) {\n", expr));
            // update debounce time for sensors used in the condition
            for (String sensorName : collectSensors(transition.getCondition())) {
                w(String.format("\t\t\t\t%sLastDebounceTime = millis();\n", sensorName));
            }
            w("\t\t\t\tcurrentState = " + transition.getNext().getName() + ";\n");
            w("\t\t\t}\n");
            return;
        }
    }

    @Override
    public void visit(TemporalTransition transition) {
        if (context.get("pass") == PASS.ONE) {
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            int delayInMS = transition.getDelayInMs();
            w(String.format("\t\t\tdelay(%d);\n", delayInMS));
            w("\t\t\tcurrentState = " + transition.getNext().getName() + ";\n");
            return;
        }
    }

    @Override
    public void visit(Action action) {
        if (context.get("pass") == PASS.ONE) {
            return;
        }
        if (context.get("pass") == PASS.TWO) {
            if (action instanceof BuzzerAction) {
                BuzzerAction bz = (BuzzerAction) action;
                int duration = bz.getLength() == BuzzerAction.LENGTH.SHORT ? 200 : 1000;
                w(String.format("\t\t\tdigitalWrite(%d, HIGH);\n", bz.getActuator().getPin()));
                w(String.format("\t\t\tdelay(%d);\n", duration));
                w(String.format("\t\t\tdigitalWrite(%d, LOW);\n", bz.getActuator().getPin()));
                return;
            }
            if (action instanceof LCDAction) {
                LCDAction lcd = (LCDAction) action;
                String message = padToWidth(lcd.getMessage(), lcdColumns).replace("\"", "\\\"");
                w(String.format("\t\t\t%s.setCursor(0, 0);\n", lcdVarName));
                w(String.format("\t\t\t%s.print(\"%s\");\n", lcdVarName, message));
                return;
            }
            if (action instanceof ActuatorAction) {
                ActuatorAction aa = (ActuatorAction) action;
                w(String.format("\t\t\tdigitalWrite(%d,%s);\n", aa.getActuator().getPin(), aa.getValue()));
                return;
            }
        }
    }

    // ---------- Helpers for conditions ----------
    private java.util.Set<String> collectSensors(Condition c) {
        java.util.Set<String> names = new java.util.HashSet<>();
        if (c == null) return names;
        if (c instanceof SensorLevelCondition) {
            names.add(((SensorLevelCondition) c).getSensor().getName());
        } else if (c instanceof PushEventCondition) {
            names.add(((PushEventCondition) c).getSensor().getName());
        } else if (c instanceof AnalogThresholdCondition) {
            names.add(((AnalogThresholdCondition) c).getSensor().getName());
        } else if (c instanceof AndCondition) {
            names.addAll(collectSensors(((AndCondition) c).getLeft()));
            names.addAll(collectSensors(((AndCondition) c).getRight()));
        } else if (c instanceof OrCondition) {
            names.addAll(collectSensors(((OrCondition) c).getLeft()));
            names.addAll(collectSensors(((OrCondition) c).getRight()));
        } else if (c instanceof NotCondition) {
            names.addAll(collectSensors(((NotCondition) c).getCondition()));
        }
        return names;
    }

    private String conditionToExpr(Condition c) {
        if (c instanceof SensorLevelCondition) {
            SensorLevelCondition slc = (SensorLevelCondition) c;
            String name = slc.getSensor().getName();
            return String.format("digitalRead(%d) == %s && %sBounceGuard", slc.getSensor().getPin(), slc.getExpectedValue(), name);
        }
        if (c instanceof PushEventCondition) {
            PushEventCondition pec = (PushEventCondition) c;
            String name = pec.getSensor().getName();
            return String.format("digitalRead(%d) == HIGH && %sBounceGuard", pec.getSensor().getPin(), name);
        }
        if (c instanceof AnalogThresholdCondition) {
            AnalogThresholdCondition at = (AnalogThresholdCondition) c;
            String pin = "A" + at.getSensor().getPin();
            String op = at.isAtLeast() ? ">=" : "<";
            return String.format("analogRead(%s) %s %d", pin, op, at.getThreshold());
        }
        if (c instanceof AndCondition) {
            AndCondition ac = (AndCondition) c;
            return String.format("(%s) && (%s)", conditionToExpr(ac.getLeft()), conditionToExpr(ac.getRight()));
        }
        if (c instanceof OrCondition) {
            OrCondition oc = (OrCondition) c;
            return String.format("(%s) || (%s)", conditionToExpr(oc.getLeft()), conditionToExpr(oc.getRight()));
        }
        if (c instanceof NotCondition) {
            NotCondition nc = (NotCondition) c;
            return String.format("!(%s)", conditionToExpr(nc.getCondition()));
        }
        return "true"; // fallback
    }

    private String padToWidth(String msg, int width) {
        if (msg == null) return "";
        if (msg.length() >= width) return msg;
        StringBuilder sb = new StringBuilder(msg);
        while (sb.length() < width) {
            sb.append(' ');
        }
        return sb.toString();
    }
}
