package org.spoofer.interpreter;

import org.spoofer.model.TurtleState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Interpreter} parses lines of string commands and their arguments into {@link Command}s and applies those commands to the given {@link TurtleState}
 */
public class Interpreter {

    private static final Logger LOG = Logger.getLogger(Interpreter.class.getName());

    // A map of the commands and their string name mappings.
    private static final Map<String, Command> commands = new HashMap<>();

    // A Map of marcos, alternative strings which are replaced in the command line.
    private static Map<String, String> macros = new HashMap<>();

    static {
        buildCommands();
        buildMacros();
    }

    /**
     * run the given line of commands and apply the commands to the state.
     * The given line must be space seperated words beginning with a valid command.
     * Each command must have any required arguments follow it.
     * Further commands may foloow the last argument of the first command, and will be inturpreted int he same fashion as the first.
     * Any number of command / argument combintaitons may be concatinated in this manner.
     * e.g. forward 10 rotate 90 forward 10 rotate -90 forward 20, is inturpreted as thre commands, each with a single argument.
     *
     * @param turtleState the state to apply the command line to.
     * @param line        the line of command(s) to inturpret.
     * @throws IllegalArgumentException
     */
    public void run(TurtleState turtleState, String line) throws Exception {
        List<String> args = parseLine(line);
        LOG.log(Level.INFO, String.format("interpreted '%s' from\n '%s'\n", args.toString(), line));

        while (!args.isEmpty()) {
            String cmd = args.get(0).trim().toLowerCase();
            args.remove(0);

            Command command = commands.get(cmd);
            if (command == null) {
                throw new IllegalArgumentException(String.format("%s is an unknown command", cmd));
            }

            command.execute(createStack(turtleState), args);
        }
    }

    private Stack createStack(TurtleState turtleState) {
        StackMap stack = new StackMap();
        stack.put("turtle", turtleState);
        stack.put("runtime", this);
        stack.put("env", System.getenv());
        return stack;
    }


    /**
     * parseLine parses the given line into a List of space sperated Strings, ignoring any spaces enclosed in quotes.
     *
     * @param line line to parse
     * @return List of seperate strings
     */
    private List<String> parseLine(String line) {
        // https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(line);
        while (m.find())
            list.add(m.group(1));

        // replace any args with "macros"
        List<String> args = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String arg = list.get(i);
            String key = arg.toLowerCase();
            if (macros.containsKey(key)) {
                List<String> macroKeys = parseLine(macros.get(key));
                if (macroKeys.isEmpty()) {
                    continue;
                }
                int last = macroKeys.size() - 1;
                if (macroKeys.get(last).endsWith("{}")) {
                    String sArg = macroKeys.remove(last);
                    sArg = sArg.substring(0, sArg.length() - 2);
                    if (i + 1 < list.size()) {
                        sArg += list.get(i + 1);
                        list.remove(i + 1);
                    }
                    macroKeys.add(sArg);
                }
                args.addAll(macroKeys);
            } else {
                args.add(arg);
            }
        }
        return args;
    }

    private static void buildCommands() {
        commands.put("forward", new ForwardCommand());
        commands.put("home", new HomeCommand());
        commands.put("rotate", new RotateCommand());
        commands.put("pen", new PenCommand());
        commands.put("turtle", new TurtleVisibleCommand());
        commands.put("clean", new CleanCommand());
        commands.put("repeat", new RepeatCommand());
        commands.put("dump", new DumpCommand());
    }

    // macros are a list of alternative text, which is replaced in the command line, with its mapped value.
    private static void buildMacros() {
        macros.put("fd", "forward");
        macros.put("back", "forward -{}");
        macros.put("bk", "forward -{}");
        macros.put("right", "rotate");
        macros.put("rt", "rotate");
        macros.put("left", "rotate -{}");
        macros.put("lt", "rotate -{}");

        macros.put("cleanscreen", "clean home");
        macros.put("cs", "clean home");
        macros.put("showturtle", "turtle true");
        macros.put("st", "turtle true");
        macros.put("hideturtle", "turtle false");
        macros.put("ht", "turtle false");
        macros.put("penup", "pen on");
        macros.put("pu", "pen false");
        macros.put("pd", "pen true");

        macros.put("go", "forward");
        macros.put("turn", "rotate");
        macros.put("draw", "pen on");
    }

}
