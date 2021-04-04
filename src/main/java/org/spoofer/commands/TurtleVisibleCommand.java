package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtleState;

import java.util.List;

public class TurtleVisibleCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing visible value.  Provide a boolean true or false.");
        }

        String arg = args.remove(0);

        boolean visible;
        if ("yes".equalsIgnoreCase(arg) || "on".equalsIgnoreCase(arg))
            visible = true;
        else if ("no".equalsIgnoreCase(arg) || "off".equalsIgnoreCase(arg))
            visible = false;
        else {
            try {
                visible = Boolean.parseBoolean(arg);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid pen state. must be on/off, up/down or true/false", e);
            }
        }

        TurtleState turtle = state.get("turtle");
        turtle.isVisible = visible;
    }
}
