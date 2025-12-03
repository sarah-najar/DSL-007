package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class TemporalTransition extends Transition {

    private int duration; // raw duration
    private TimeUnit unit = TimeUnit.ms;

    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    public TimeUnit getUnit() { return unit; }
    public void setUnit(TimeUnit unit) { this.unit = unit; }

    public int getDelayInMs() { return duration * (unit != null ? unit.inMillis : 1); }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

