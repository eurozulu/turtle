package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.Macro;
import org.spoofer.interpreter.State;

import java.util.List;
import java.util.Map;

/**
 * {@link PatternCommand} defines a new Pattern.
 * A Pattern is a function like concept, giving a unique name to a predefined set of commands.
 * Pattern uses two arguments, the name of the new pattern and a code block, a set of commands enclosed in Braces []
 */
public class PatternCommand implements Command {

    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing pattern name.  Provide a unique name for the pattern.");
        }
        String name = args.remove(0);

        if (args.isEmpty() || !"[".equals(args.get(0))) {
            throw new IllegalArgumentException("missing or empty pattern block.  Provide a block of commands embraced in [...]");
        }
        List<String> codeBlock = Interpreter.findCodeBlock(args);
        if (codeBlock.isEmpty()) {
            throw new IllegalArgumentException("missing or empty pattern block.  Provide a block of commands embraced in [...]");
        }
        // Clean code block from args
        for (int i=0; i < codeBlock.size() && !args.isEmpty(); i++) {
            args.remove(0);
        }
        // strip off block tokens
        codeBlock.remove(0);
        codeBlock.remove(codeBlock.size() - 1);

        Map<String, Macro> patterns = state.get("runtime.patterns");
        String cmd = String.join(" ", codeBlock);
        patterns.put(name, new Macro(cmd));
        System.out.printf("pattern '%s' created as '%s'\n", name, cmd);
    }

}
