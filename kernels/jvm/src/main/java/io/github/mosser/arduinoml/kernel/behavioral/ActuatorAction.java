package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.Actuator;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;

public class ActuatorAction extends Action {
    private SIGNAL value;
    private Actuator actuator;

    public SIGNAL getValue() { return value; }

    public void setValue(SIGNAL value) { this.value = value; }

    public Actuator getActuator() { return actuator; }

    public void setActuator(Actuator actuator) { this.actuator = actuator; }
}

