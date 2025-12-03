package io.github.mosser.arduinoml.embedded.java.dsl;


import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.*;

import java.util.Optional;

public class InstructionBuilder {

    private StateBuilder parent;

    private String targetName;

    InstructionBuilder(StateBuilder parent, String target) {
        this.parent = parent;
        this.targetName = target;
    }

    public StateBuilder toHigh() {
        Actuator act = resolveActuator(targetName);
        ActuatorAction a = new ActuatorAction();
        a.setActuator(act);
        a.setValue(SIGNAL.HIGH);
        return goUp(a);
    }

    public StateBuilder toLow() {
        Actuator act = resolveActuator(targetName);
        ActuatorAction a = new ActuatorAction();
        a.setActuator(act);
        a.setValue(SIGNAL.LOW);
        return goUp(a);
    }

    public StateBuilder shortBeep() {
        Actuator act = resolveActuator(targetName);
        if (!(act instanceof Buzzer))
            throw new IllegalArgumentException("shortBeep can only be used with a buzzer actuator");
        BuzzerAction a = new BuzzerAction();
        a.setActuator((Buzzer) act);
        a.setLength(BuzzerAction.LENGTH.SHORT);
        return goUp(a);
    }

    public StateBuilder longBeep() {
        Actuator act = resolveActuator(targetName);
        if (!(act instanceof Buzzer))
            throw new IllegalArgumentException("longBeep can only be used with a buzzer actuator");
        BuzzerAction a = new BuzzerAction();
        a.setActuator((Buzzer) act);
        a.setLength(BuzzerAction.LENGTH.LONG);
        return goUp(a);
    }

    public StateBuilder display(String text) {
        Actuator act = resolveActuator(targetName);
        if (!(act instanceof LCDScreen))
            throw new IllegalArgumentException("display can only be used with an LCD screen actuator");
        LCDAction a = new LCDAction();
        a.setActuator((LCDScreen) act);
        a.setMessage(text);
        return goUp(a);
    }

    private Actuator resolveActuator(String name) {
        Optional<Actuator> opt = parent.parent.findActuator(name);
        return opt.orElseThrow(() -> new IllegalArgumentException("Illegal actuator: [" + name + "]"));
    }

    private StateBuilder goUp(Action action) {
        parent.local.getActions().add(action);
        return parent;
    }

}
