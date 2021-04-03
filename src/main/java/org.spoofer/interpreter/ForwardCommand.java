package org.spoofer.interpreter;

import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import java.util.List;

public class ForwardCommand implements Command {

    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing forward value.  Provide a numer of units to move.");
        }

        int distance;
        try {
            distance = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid forward value. Must be a number", e);
        }
        args.remove(0);

        TurtlePosition position = stack.get("turtle.turtlePosition");

        double radAngle = Math.toRadians(position.rotation);
        long x = Math.round(position.x + distance * Math.cos(radAngle));
        long y = Math.round(position.y + distance * Math.sin(radAngle));
        if (x > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid forward value, too large");
        }
        if (y > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid forward value, too large");
        }
        TurtleState turtle = stack.get("turtle");
        turtle.moveTurtle(position.moved((int)x, (int)y));
    }
}
