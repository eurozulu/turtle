package org.spoofer.interpreter;

import java.util.List;


/**
 * Macro is a representation of one or more commands in a single command line.
 * The command line may contain argument place holders in the form of ARG_TOKENs.
 * These tokens are replaced with argument values using the {@link #Command(List)} method.
 */
public interface Macro {

    /**
     * Token used as a placeholder to represent an argument value in the command line
     */
    String ARG_TOKEN = "{}";

    /**
     * Command retuns the 'raw', unargumented command line (With its TOKENS, if present)
     * @return the command line with TOKENs.
     */
    String Command();

    /**
     * Command will return the command line with the tokens replaced with arguments from the given argument list.
     * Each Tokens are replaced, left to right, with the argument in the list with the corresponding index to the tokens position.
     * i.e. first left most token gets first argument, second token gets second argument and so on.
     * If there are not enough arguments are provided to replace all the tokens, the token(s) is/are replaced with an empty string.
     *
     * Any arguments consumed into tokens are removed from the given argument list.
     *
     * If the macro contains no ARG_TOKENs, this is the same as calling {@link #Command()}
     * @param args The argument values to use to replace the ARG_TOKENs
     * @return the argmented (Is there such a word? :-) command line.
     */
    String Command(List<String> args);
}
