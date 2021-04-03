package org.spoofer.interpreter;

import org.spoofer.model.TurtlePosition;

import java.util.List;

public class PenCommand implements Command {
    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No pen value found.  Provide on/off or true/falsefor a pen state");
        }
        String state = args.get(0);
        args.remove(0);

        boolean penState;
        if ("on".equalsIgnoreCase(state))
            penState = true;
        else if ("off".equalsIgnoreCase(state))
            penState = false;
        else {
            try {
                penState = Boolean.parseBoolean(state);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("invalid pen state. must be on/off or true/false", e);
            }
        }
        TurtlePosition position = stack.get("turtle.TurtlePosition");
        position.imprinted = penState;
    }
}
