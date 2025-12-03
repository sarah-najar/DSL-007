package io.github.mosser.arduinoml.kernel.behavioral;

public class NotCondition extends Condition {
    private Condition condition;

    public Condition getCondition() { return condition; }
    public void setCondition(Condition condition) { this.condition = condition; }
}

