package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.AnalogSensor;

public class AnalogThresholdCondition extends Condition {
    private AnalogSensor sensor;
    private int threshold; // 0..1023
    private boolean atLeast = true; // >= when true, < otherwise

    public AnalogSensor getSensor() { return sensor; }
    public void setSensor(AnalogSensor sensor) { this.sensor = sensor; }

    public int getThreshold() { return threshold; }
    public void setThreshold(int threshold) { this.threshold = threshold; }

    public boolean isAtLeast() { return atLeast; }
    public void setAtLeast(boolean atLeast) { this.atLeast = atLeast; }
}

