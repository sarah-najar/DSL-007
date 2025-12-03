package io.github.mosser.arduinoml.kernel.behavioral;

import io.github.mosser.arduinoml.kernel.generator.Visitable;
import io.github.mosser.arduinoml.kernel.generator.Visitor;

// Action is now an abstract concept. Concrete actions are specialized
// (ActuatorAction, BuzzerAction, LCDAction).
public abstract class Action implements Visitable {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
