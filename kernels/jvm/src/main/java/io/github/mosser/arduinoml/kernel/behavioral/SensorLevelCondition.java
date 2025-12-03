package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

public class SensorLevelCondition extends Condition {
    private Sensor sensor;
    private SIGNAL expectedValue;

    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }

    public SIGNAL getExpectedValue() { return expectedValue; }
    public void setExpectedValue(SIGNAL expectedValue) { this.expectedValue = expectedValue; }
}

