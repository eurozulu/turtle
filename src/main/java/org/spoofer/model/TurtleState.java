package org.spoofer.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link TurtleState} maintains the current state of the turtle and a history of its previous states.
 */
public class TurtleState {

    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;

    // Area in which the turtle can move
    private final Dimension contraint = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    // State History
    private final List<TurtlePosition> turtlePath = new ArrayList<>();

    // If turtle should be drawn on screen (Has no effect on Turtle state)
    public boolean isTurtleVisible = true;

    public TurtleState() {
        centerTurtle();
    }

    /**
     * gets the current state of the turtle
     * @return the current state
     */
    public TurtlePosition getTurtlePosition() {
        if (turtlePath.isEmpty()) {
            centerTurtle();
        }
        return turtlePath.get(turtlePath.size() - 1);
    }

    /**
     * Checks if the turtle path is empty.  Path must have two or more positions to be not empty.
     * @return true if the path has at least two positions (forming a single line), false otherwise.
     */
    public boolean IsEmpty() {
        return turtlePath.size() < 2;
    }

    /**
     * gets the full history of turtle states
     * @return state history
     */
    public TurtlePosition[] getTurtlePath() {
        TurtlePosition[] positions = new TurtlePosition[turtlePath.size()];
        turtlePath.toArray(positions);
        return positions;
    }

    /**
     * clears the state history of the turtle.
     *
     */
    public void clearPath() {
        TurtlePosition pos = getTurtlePosition();
        turtlePath.clear();
        turtlePath.add(pos);
    }

    public void centerTurtle() {
        setConstraint(contraint); // sanity check constraints
        int x = contraint.width / 2;
        int y = contraint.height / 2;

        TurtlePosition homePos;
        if (!turtlePath.isEmpty() ) {
            TurtlePosition pos = getTurtlePosition();
            homePos = pos.clone(x, y);
            pos.imprinted = false;

        } else
              homePos = new TurtlePosition(x, y);
        turtlePath.add(homePos);
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
