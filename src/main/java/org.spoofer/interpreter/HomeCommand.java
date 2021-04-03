package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

import java.util.List;

public class HomeCommand implements Command {
    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        TurtleState turtle = stack.get("turtle");
        turtle.centerTurtle();
    }
}
