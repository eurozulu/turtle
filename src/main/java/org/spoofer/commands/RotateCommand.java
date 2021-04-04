package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;

import java.util.List;

public class RotateCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No rotate value found.  Provide an angle, relative to the current rotation");
        }

        double rotate = Command.parseArgDouble(args.remove(0));

        while (rotate < 0) {
            rotate += 360;
        }
        TurtlePosition position = state.get("turtle.turtlePosition");
        position.rotation = (position.rotation + rotate) % 360;
    }
}
