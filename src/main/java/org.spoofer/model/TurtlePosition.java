package org.spoofer.model;

public class TurtlePosition {
    public int x = 0;
    public int y = 0;
    public double rotation = 0;
    public boolean imprinted = false;

    @Override
    public String toString() {
        return String.format("x=%d,y=%d,angle=%d,pen=%b", x, y, Math.round(rotation), imprinted);
    }

    private TurtlePosition() {
    }

    protected TurtlePosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public TurtlePosition moved(int x, int y) {
        TurtlePosition position = new TurtlePosition();
        position.x = x;
        position.y = y;
        position.rotation = rotation;
        position.imprinted = imprinted;
        return position;
    }


}

