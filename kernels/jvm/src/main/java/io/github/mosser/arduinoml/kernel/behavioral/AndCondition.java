package io.github.mosser.arduinoml.kernel.behavioral;

public class AndCondition extends Condition {
    private Condition left;
    private Condition right;

    public Condition getLeft() { return left; }
    public void setLeft(Condition left) { this.left = left; }

    public Condition getRight() { return right; }
    public void setRight(Condition right) { this.right = right; }
}

