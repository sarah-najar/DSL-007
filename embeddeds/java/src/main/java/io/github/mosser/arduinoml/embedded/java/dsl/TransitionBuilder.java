package io.github.mosser.arduinoml.embedded.java.dsl;


import io.github.mosser.arduinoml.kernel.behavioral.*;
import io.github.mosser.arduinoml.kernel.structural.SIGNAL;
import io.github.mosser.arduinoml.kernel.structural.Sensor;

public class TransitionBuilder {

    private final TransitionTableBuilder parent;
    private final State sourceState;

    private enum MODE { NONE, SIGNAL, TIME, CONDITION }
    private MODE mode = MODE.NONE;

    private Sensor sensor;
    private SIGNAL sensorValue;
    private io.github.mosser.arduinoml.kernel.behavioral.Condition condition;
    private Integer delay;
    private TimeUnit unit;

    TransitionBuilder(TransitionTableBuilder parent, String source) {
        this.parent = parent;
        this.sourceState = parent.findState(source);
    }

    public TransitionBuilder when(String sensor) {
        this.mode = MODE.SIGNAL;
        this.sensor = parent.findSensor(sensor);
        return this;
    }

    public TransitionBuilder isHigh() {
        if (mode != MODE.SIGNAL) throw new IllegalStateException("isHigh() requires a preceding when(sensor)");
        this.sensorValue = SIGNAL.HIGH;
        return this;
    }

    public TransitionBuilder isLow() {
        if (mode != MODE.SIGNAL) throw new IllegalStateException("isLow() requires a preceding when(sensor)");
        this.sensorValue = SIGNAL.LOW;
        return this;
    }

    public TransitionBuilder after(int milliseconds) {
        if (milliseconds < 0) throw new IllegalArgumentException("Delay must be >= 0");
        this.mode = MODE.TIME;
        this.delay = milliseconds;
        this.unit = TimeUnit.ms;
        return this;
    }

    public TransitionBuilder after(int value, TimeUnit unit) {
        if (value < 0) throw new IllegalArgumentException("Delay must be >= 0");
        this.mode = MODE.TIME;
        this.delay = value;
        this.unit = unit;
        return this;
    }

    public TransitionTableBuilder goTo(String state) {
        Transition t;
        switch (mode) {
            case SIGNAL:
                if (sensor == null || sensorValue == null)
                    throw new IllegalStateException("Incomplete signal transition (sensor/value)");
                ConditionalTransition st = new ConditionalTransition();
                SensorLevelCondition cond = new SensorLevelCondition();
                cond.setSensor(sensor);
                cond.setExpectedValue(sensorValue);
                st.setCondition(cond);
                st.setNext(parent.findState(state));
                t = st;
                break;
            case TIME:
                if (delay == null)
                    throw new IllegalStateException("Incomplete time transition (delay)");
                TemporalTransition tt = new TemporalTransition();
                tt.setDuration(delay);
                tt.setUnit(unit == null ? TimeUnit.ms : unit);
                tt.setNext(parent.findState(state));
                t = tt;
                break;
            case CONDITION:
                if (condition == null)
                    throw new IllegalStateException("No composite condition configured");
                ConditionalTransition ct = new ConditionalTransition();
                ct.setCondition(condition);
                ct.setNext(parent.findState(state));
                t = ct;
                break;
            default:
                throw new IllegalStateException("Transition must be configured (when(...) or after(...))");
        }
        sourceState.addTransition(t);
        return parent;
    }

    // --------- Convenience APIs for composite conditions ---------
    public TransitionBuilder whenBothHigh(String sensorA, String sensorB) {
        Sensor a = parent.findSensor(sensorA);
        Sensor b = parent.findSensor(sensorB);
        SensorLevelCondition ca = new SensorLevelCondition(); ca.setSensor(a); ca.setExpectedValue(SIGNAL.HIGH);
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(b); cb.setExpectedValue(SIGNAL.HIGH);
        AndCondition and = new AndCondition(); and.setLeft(ca); and.setRight(cb);
        this.mode = MODE.CONDITION; this.condition = and; return this;
    }

    public TransitionBuilder whenButtonAndPotAtLeast(String button, String pot, int threshold) {
        Sensor b = parent.findSensor(button);
        Sensor p = parent.findSensor(pot);
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(b); cb.setExpectedValue(SIGNAL.HIGH);
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor((io.github.mosser.arduinoml.kernel.structural.AnalogSensor) p);
        ap.setThreshold(threshold); ap.setAtLeast(true);
        AndCondition and = new AndCondition(); and.setLeft(cb); and.setRight(ap);
        this.mode = MODE.CONDITION; this.condition = and; return this;
    }

    public TransitionBuilder whenEitherButtonLowOrPotBelow(String button, String pot, int threshold) {
        Sensor b = parent.findSensor(button);
        Sensor p = parent.findSensor(pot);
        SensorLevelCondition cb = new SensorLevelCondition(); cb.setSensor(b); cb.setExpectedValue(SIGNAL.LOW);
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor((io.github.mosser.arduinoml.kernel.structural.AnalogSensor) p);
        ap.setThreshold(threshold); ap.setAtLeast(false); // strictly below
        OrCondition or = new OrCondition(); or.setLeft(cb); or.setRight(ap);
        this.mode = MODE.CONDITION; this.condition = or; return this;
    }

    // Analog-only helpers
    public TransitionBuilder whenPotAtLeast(String pot, int threshold) {
        Sensor p = parent.findSensor(pot);
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor((io.github.mosser.arduinoml.kernel.structural.AnalogSensor) p);
        ap.setThreshold(threshold); ap.setAtLeast(true);
        this.mode = MODE.CONDITION; this.condition = ap; return this;
    }

    public TransitionBuilder whenPotBelow(String pot, int threshold) {
        Sensor p = parent.findSensor(pot);
        AnalogThresholdCondition ap = new AnalogThresholdCondition();
        ap.setSensor((io.github.mosser.arduinoml.kernel.structural.AnalogSensor) p);
        ap.setThreshold(threshold); ap.setAtLeast(false);
        this.mode = MODE.CONDITION; this.condition = ap; return this;
    }
}
