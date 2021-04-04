package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;

import java.util.List;

public class UndoCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        int count = 1;
        if (!args.isEmpty()) {
            try {
                count = Command.parseArgInt(args.get(0));
                args.remove(0);
            } catch (IllegalArgumentException e) {
                count = 1;
            }
        }
        List<TurtlePosition> path = state.get("turtle.path");

        for (int i = 0; i < count; i++) {
            if (path.isEmpty())
                break;
            path.remove(path.size() - 1);
        }
    }
}
