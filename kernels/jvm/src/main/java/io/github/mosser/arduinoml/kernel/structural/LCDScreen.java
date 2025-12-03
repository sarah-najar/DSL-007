package io.github.mosser.arduinoml.kernel.structural;

import io.github.mosser.arduinoml.kernel.generator.Visitor;

public class LCDScreen extends Actuator {

    private int columns;
    private int rows;

    public int getColumns() { return columns; }

    public void setColumns(int columns) { this.columns = columns; }

    public int getRows() { return rows; }

    public void setRows(int rows) { this.rows = rows; }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}

