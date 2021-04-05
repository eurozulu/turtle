package org.spoofer.model;

import java.awt.*;

public class TurtlePosition {
    public static final Color DEFAULT_COLOUR = Color.blue;

    public final int x;
    public final int y;

    public double rotation = 0;
    public Color paintColour = DEFAULT_COLOUR;
    public boolean imprinted = false;

    @Override
    public String toString() {
        return String.format("x=%d,y=%d,angle=%d,pen=%b", x, y, Math.round(rotation), imprinted);
    }

    protected TurtlePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TurtlePosition clone(int x, int y) {
        TurtlePosition position = new TurtlePosition(x, y);
        position.rotation = rotation;
        position.paintColour = paintColour;
        position.imprinted = imprinted;
        return position;
    }


}

