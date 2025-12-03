package io.github.mosser.arduinoml.kernel.validation;

import io.github.mosser.arduinoml.kernel.App;
import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.Brick;
import io.github.mosser.arduinoml.kernel.structural.LCDScreen;

import java.util.*;

public class ModelValidator {

    public static List<String> validate(App app) {
        List<String> errors = new ArrayList<>();

        if (app.getInitial() == null) {
            errors.add("Initial state is not set");
        } else if (!app.getStates().contains(app.getInitial())) {
            errors.add("Initial state is not part of application states");
        }

        // Name uniqueness for states
        Set<String> stateNames = new HashSet<>();
        for (State s : app.getStates()) {
            if (!stateNames.add(s.getName())) {
                errors.add("Duplicate state name: " + s.getName());
            }
        }

        // Name uniqueness for bricks
        Set<String> brickNames = new HashSet<>();
        for (Brick b : app.getBricks()) {
            if (!brickNames.add(b.getName())) {
                errors.add("Duplicate brick name: " + b.getName());
            }
        }

        for (State s : app.getStates()) {
            for (Action a : s.getActions()) {
                if (a instanceof LCDAction) {
                    LCDAction la = (LCDAction) a;
                    LCDScreen lcd = la.getActuator();
                    int capacity = lcd.getColumns() * lcd.getRows();
                    String msg = la.getMessage() == null ? "" : la.getMessage();
                    if (msg.length() > capacity) {
                        errors.add("LCD message too long for screen '"+lcd.getName()+"' (capacity "+capacity+")");
                    }
                }
            }

            for (Transition t : s.getTransitions()) {
                if (t.getNext() == null) {
                    errors.add("Transition in state '"+s.getName()+"' has no target state");
                } else if (!app.getStates().contains(t.getNext())) {
                    errors.add("Transition in state '"+s.getName()+"' targets a state outside of the app");
                }
                if (t instanceof TemporalTransition) {
                    TemporalTransition tt = (TemporalTransition) t;
                    if (tt.getDuration() < 0) {
                        errors.add("Temporal transition delay must be >= 0 in state '"+s.getName()+"'");
                    }
                } else if (t instanceof ConditionalTransition) {
                    ConditionalTransition ct = (ConditionalTransition) t;
                    if (ct.getCondition() == null) {
                        errors.add("Conditional transition in state '"+s.getName()+"' has no condition");
                    }
                }
            }
        }

        return errors;
    }
}
