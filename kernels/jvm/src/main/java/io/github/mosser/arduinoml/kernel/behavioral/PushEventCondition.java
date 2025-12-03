package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.structural.Sensor;

// Simple push event condition (treated as button HIGH with debounce in generator)
public class PushEventCondition extends Condition {
    private Sensor sensor;

    public Sensor getSensor() { return sensor; }
    public void setSensor(Sensor sensor) { this.sensor = sensor; }
}

