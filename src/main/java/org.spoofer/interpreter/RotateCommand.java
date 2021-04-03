package org.spoofer.interpreter;

import org.spoofer.model.TurtlePosition;

import java.util.List;

public class RotateCommand implements Command {
    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No rotate value found.  Provide an angle, relative to the current rotation");
        }
        String angle = args.get(0);
        args.remove(0);

        double rotate;
        try {
            rotate = Double.parseDouble(angle);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("inbalid rotate angle. must be a number", e);
        }
        while (rotate < 0) {
            rotate += 360;
        }
        TurtlePosition position = stack.get("turtle.turtlePosition");
        position.rotation = (position.rotation + rotate) % 360;
    }
}
