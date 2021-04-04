package org.spoofer.commands;

import org.spoofer.interpreter.State;

import java.util.List;

/**
 * {@link Command} A command consumes zero or more arguments and carries out a specific action using the given {@link State}
 */
public interface Command {
    /**
     * execute this common on the given state, using any given arguments.
     * The Command can consume zero, one or more arguments.
     * @param state The stack containing the current state to act upon
     * @param args optional arguments the command may or may not require
     * @throws IllegalArgumentException If any given argument is missing, or invalid
     * @throws IllegalStateException If the command fails to carry out the operation.
     */
    void execute(State state, List<String> args) throws Exception;

    static int parseArgInt(String s) throws IllegalArgumentException {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid argument. Must be a number", e);
        }
    }
    static double parseArgDouble(String s) throws IllegalArgumentException {
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid argument. Must be a number", e);
        }
    }
    static boolean parseArgBool(String s) throws IllegalArgumentException {
        try {
            return Boolean.parseBoolean(s);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("invalid argument. Must be a boolean (true/false)", e);
        }
    }
}
