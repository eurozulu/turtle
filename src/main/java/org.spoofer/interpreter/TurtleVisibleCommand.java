package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

import java.util.List;

public class TurtleVisibleCommand implements Command {
    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing visible value.  Provide a boolean true or false.");
        }

        boolean visible;
        try {
            visible = Boolean.parseBoolean(args.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid visible value. Must be a boolean", e);
        }
        args.remove(0);

        TurtleState turtle = stack.get("turtle");
        turtle.isVisible = visible;
    }
}
