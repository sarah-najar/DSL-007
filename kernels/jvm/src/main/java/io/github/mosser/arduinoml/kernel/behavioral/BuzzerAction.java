package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.Buzzer;

public class BuzzerAction extends Action {
    public enum LENGTH { SHORT, LONG }

    private Buzzer actuator;
    private LENGTH length = LENGTH.SHORT;

    public Buzzer getActuator() { return actuator; }
    public void setActuator(Buzzer actuator) { this.actuator = actuator; }

    public LENGTH getLength() { return length; }
    public void setLength(LENGTH length) { this.length = length; }
}

