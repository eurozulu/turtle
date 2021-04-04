package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;

import java.util.List;

public class PenCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No pen value found.  Provide on/off or true/falsefor a pen szState");
        }
        String arg = args.remove(0);

        boolean penState;
        if ("down".equalsIgnoreCase(arg) || "on".equalsIgnoreCase(arg))
            penState = true;
        else if ("up".equalsIgnoreCase(arg) || "off".equalsIgnoreCase(arg))
            penState = false;
        else {
            try {
                penState = Boolean.parseBoolean(arg);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid pen state. must be on/off, up/down or true/false", e);
            }
        }
        TurtlePosition position = state.get("turtle.TurtlePosition");
        position.imprinted = penState;
    }
}
