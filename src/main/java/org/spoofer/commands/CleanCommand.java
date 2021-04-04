package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.State;
import org.spoofer.model.TurtleState;

import java.util.List;

public class CleanCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        TurtleState turtle = state.get("turtle");
        turtle.clearPath();

        if (!args.isEmpty()) {
            String cmd = args.get(0).toLowerCase();
            if ("patterns".equals(cmd)) {
                args.remove(0);
                Interpreter runtime = state.get("runtime");
                runtime.getPatterns().clear();
            }
        }
    }
}
