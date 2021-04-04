package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.State;
import org.spoofer.model.TurtleState;

import java.util.List;

public class RepeatCommand implements Command {

    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing repeat count.  Provide a number of times to repeat.");
        }
        int repeat = Command.parseArgInt(args.remove(0));
        if (repeat < 0) {
            throw new IllegalArgumentException("invalid repeat count. Must be a positive number");
        }

        // Extract code block and tokens from args
        List<String> codeBlock = Interpreter.findCodeBlock(args);
        if (codeBlock.isEmpty()) {
            throw new IllegalArgumentException("missing repeat block.  Provide a block of commands embraced in [...]");
        }
        args.removeAll(codeBlock);
        // strip off block tokens
        codeBlock.remove(0);
        codeBlock.remove(codeBlock.size() - 1);

        String cmdLine = String.join(" ", codeBlock);

        TurtleState turtle = state.get("turtle");
        Interpreter runtime = state.get("runtime");

        for (int i = 0; i < repeat; i++) {
            runtime.run(turtle, cmdLine);
        }
    }
}
