package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

/**
 * Process represents a single run of commands on its given turtle state.
 */
public class Process {
    public interface ProcessComplete {
        void completed();
    }

    private final State state;

    private final String command;

    private final ProcessComplete listener;

    public Process(State state, String command, ProcessComplete listener) {
        this.state = state;
        this.command = command;
        this.listener = listener;
    }

    @Override
    public String toString() {
        int i = command.indexOf("\n");
        return String.format("Process: cmd='%s'", i > 0 ? command + "..." : command);
    }

    /**
     * Command for this process
     * The given command must be space separated commands and numbers, beginning with a valid command.
     * Each command must have any required arguments follow it.
     * Further commands may follow the last argument of the previous command, and will be interpreted in the same fashion as the previous command.
     * Any number of command / argument combintaitons may be concatinated in this manner.
     * e.g. forward 10 rotate 90 forward 10 rotate -90 forward 20, is interpreted as three commands, each with a single argument.
     * @return The comand line command for the process
     */
    public String getCommand() {
        return command;
    }

    public State getState() {
        return state;
    }

    public void notifyComplete() {
        if (null != listener)
            listener.completed();
    }
}
