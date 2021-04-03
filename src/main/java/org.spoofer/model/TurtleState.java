package org.spoofer.model;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

public class TurtleState {

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    private final Dimension contraint = new Dimension();
    private final List<TurtlePosition> turtlePath = new ArrayList<>();

    public boolean isVisible = true;

    public TurtleState() {
        clearPath();
    }

    public TurtlePosition getTurtlePosition() {
        if (turtlePath.isEmpty())
            return null;
        return turtlePath.get(turtlePath.size() - 1);
    }

    public TurtlePosition[] getTurtlePath() {
        TurtlePosition[] positions = new TurtlePosition[turtlePath.size()];
        turtlePath.toArray(positions);
        return positions;
    }

    public void clearPath() {
        turtlePath.clear();
        centerTurtle();
    }

    public void centerTurtle() {
        setConstraint(contraint); // sanity check constraints
        turtlePath.add(new TurtlePosition(contraint.width / 2, contraint.height / 2));
    }

    public void setConstraint(Dimension size) {
        contraint.width = size.width > 0 ? size.width : DEFAULT_WIDTH;
        contraint.height = size.height > 0 ? size.height : DEFAULT_HEIGHT;
    }

    public Dimension getContraint() {
        return contraint;
    }

    public void moveTurtle(TurtlePosition position) throws IndexOutOfBoundsException {
        if (position.x < 0 || position.x >= contraint.width) {
            throw new IndexOutOfBoundsException("position x is out of bounds");
        }
        if (position.y < 0 || position.y >= contraint.height) {
            throw new IndexOutOfBoundsException("position y is out of bounds");
        }
        turtlePath.add(position);
    }


}
