package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import java.util.List;

public class ForwardCommand implements Command {

    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing forward value.  Provide a number of units to move.");
        }

        int distance = Command.parseArgInt(args.remove(0));
        TurtlePosition position = state.get("turtle.turtlePosition");

        // Calculate new turtle position based on current rotation and position
        double radAngle = Math.toRadians(position.rotation);
        long x = Math.round(position.x + (double)distance * Math.sin(radAngle));
        long y = Math.round(position.y + (double)-distance * Math.cos(radAngle));
        if (x > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid forward value, too large");
        }
        if (y > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("invalid forward value, too large");
        }
        TurtleState turtle = state.get("turtle");
        turtle.moveTurtle(position.moved((int)x, (int)y));
    }
}
