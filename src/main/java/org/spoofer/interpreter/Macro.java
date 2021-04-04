package org.spoofer.interpreter;

import java.util.List;

public class Macro {
    public static final String ARG_TOKEN = "{}";

    private final String command;

    public Macro(String command) {
        this.command = command;
    }

    public String Command() { return command;}

    public String toString(List<String> args) {
        String s = Command();
        while (s.contains(ARG_TOKEN)) {
            s = s.replace(ARG_TOKEN, !args.isEmpty() ? args.remove(0) : "");
        }
        return s;
    }

    @Override
    public String toString() {
        return String.format("%s:Command: %s", Macro.class.getName(), command);
    }
}
