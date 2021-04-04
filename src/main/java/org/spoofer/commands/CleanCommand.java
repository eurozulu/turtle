package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtleState;

import java.util.List;

public class CleanCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        TurtleState turtle = state.get("turtle");
        turtle.clearPath();
    }
}
