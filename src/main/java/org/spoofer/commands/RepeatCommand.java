package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.Process;
import org.spoofer.interpreter.Runtime;
import org.spoofer.interpreter.State;

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

        // Clean code block from args
        for (int i = 0; i < codeBlock.size() && !args.isEmpty(); i++) {
            args.remove(0);
        }
        // strip off block tokens
        codeBlock.remove(0);
        codeBlock.remove(codeBlock.size() - 1);

        String cmdLine = String.join(" ", codeBlock);

        Interpreter runtime = state.get("runtime");
        Process p = new Process(state, cmdLine, null);
        for (int i = 0; i < repeat; i++) {
            runtime.run(p);
        }
    }
}
