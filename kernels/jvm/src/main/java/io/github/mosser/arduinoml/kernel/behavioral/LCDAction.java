package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.LCDScreen;

public class LCDAction extends Action {
    private LCDScreen actuator;
    private String message;

    public LCDScreen getActuator() { return actuator; }
    public void setActuator(LCDScreen actuator) { this.actuator = actuator; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}

