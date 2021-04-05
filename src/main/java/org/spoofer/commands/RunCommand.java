package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.Process;
import org.spoofer.interpreter.State;
import org.spoofer.misc.FileTools;

import java.util.List;

/**
 * {@link RunCommand} will execute an external file with the file path given in the given argument.
 */
public class RunCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("missing filename of logo file to run");
        }

        System.out.println(System.getProperty("user.dir"));

        String cmdline = FileTools.readFile(args.remove(0));
        Interpreter runtime = state.get("runtime");
        runtime.run(new Process(state, cmdline, null));
    }
}
