package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

import java.util.List;

public class RepeatCommand implements Command {

    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing repeat count.  Provide a number of time to repeat.");
        }
        int repeat;
        try {
            repeat = Integer.parseInt(args.get(0));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid repeat count. Must be a number", e);
        }
        args.remove(0);

        if (args.isEmpty() || !args.get(0).startsWith("[")) {
            throw new IllegalArgumentException("missing repeat statement.  Provide serias of command embraced in '[...]'");
        }
        int closeIndex = findClosingIndex(args);
        if (closeIndex < 0) {
            throw new IllegalArgumentException("missing repeat statement closing bracket.  Provide serias of command embraced in '[...]'");
        }
        String cmdLine = String.join(" ", args.subList(0, closeIndex + 1));
        cmdLine = cmdLine.substring(1, cmdLine.length() - 1); // clean off [ ]
        for (int i = 0; i <= closeIndex; i++) {
            args.remove(0);
        };

        TurtleState turtle = stack.get("turtle");
        Interpreter runtime = stack.get("runtime");

        for (int i = 0; i < repeat; i ++) {
            runtime.run(turtle, cmdLine);
        }

    }

    private int findClosingIndex(List<String> args) {
        int index = 0;
        while (index < args.size()) {
            if (args.get(index).endsWith("]")) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
