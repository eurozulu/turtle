package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

/**
 * {@link State} is a machine state passed to each command to act upon.
 * The State contains dot delimited named properties including the {@link org.spoofer.model.TurtleState}, and runtime state {@link Interpreter}.
 *
 * The items in the state may be {@link java.util.Map}s or generic beans.
 * Their named properties are accessed using the standard convention of dropping the 'get' part of method names.
 * e.g. to access the {@link TurtleState#getTurtlePosition()} method, use state.get("turtle.turtlePosition");
 *
 * Provided properties are:
 * "turtle"     The TurtleState
 * "runtime"    The current Interpreter
 * "ENV"        The system Environment variables.
 */
public interface State {
    <T> T get(String name);
}
