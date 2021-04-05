package org.spoofer.interpreter;

import java.util.List;

/**
 * {@link SimpleMacro} is a simple implementation of a Macro.
 */
public class SimpleMacro implements Macro {

    private final String command;

    public SimpleMacro(String command) {
        this.command = command;
    }

    public String Command() { return command;}

    public String Command(List<String> args) {
        String s = Command();
        while (s.contains(ARG_TOKEN)) {
            s = s.replace(ARG_TOKEN, !args.isEmpty() ? args.remove(0) : "");
        }
        return s;
    }

    @Override
    public String toString() {
        return String.format("%s:Command: %s", SimpleMacro.class.getName(), command);
    }
}
