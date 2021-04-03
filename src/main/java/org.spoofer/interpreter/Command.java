package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

import java.util.List;

/**
 * {@link Command} A command consumes zero or more arguments and carries out a specific action using the given {@link Stack}
 */
public interface Command {
    /**
     * execute this common on the given state, using any given arguments.
     * The Command can consume zero, one or more arguments.
     * @param stack The stack containing the current state to act upon
     * @param args optional arguments the command may or may not require
     * @throws IllegalArgumentException If any given argument is missing, or invalid
     * @throws IllegalStateException If the command fails to carry out the operation.
     */
    void execute(Stack stack, List<String> args) throws Exception;
}
