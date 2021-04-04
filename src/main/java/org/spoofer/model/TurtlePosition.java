package org.spoofer.model;

public class TurtlePosition {
    public final int x;
    public final int y;

    public double rotation = 0;
    public boolean imprinted = false;

    @Override
    public String toString() {
        return String.format("x=%d,y=%d,angle=%d,pen=%b", x, y, Math.round(rotation), imprinted);
    }

    protected TurtlePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TurtlePosition moved(int x, int y) {
        TurtlePosition position = new TurtlePosition(x, y);
        position.rotation = rotation;
        position.imprinted = imprinted;
        return position;
    }


}

