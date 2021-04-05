package org.spoofer.interpreter;

import org.spoofer.commands.*;
import org.spoofer.model.TurtleState;

import java.util.*;
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
    private static final Map<String, Macro> macros = new HashMap<>();

    static {
        buildCommands();
        buildMacros();
    }

    // patterns maintains a mapping of user defined, named patterns (functions), mapped to the macro to run.
    private final Map<String, Macro> patterns = new HashMap<>();


    /**
     * gets the mapped patterns of this {@link Interpreter}
     *
     * @return all the user defined patterns
     */
    public Map<String, Macro> getPatterns() {
        return patterns;
    }


    /**
     * run the given line of commands and apply the commands to the state.
     * The given line must be space separated commands and numbers, beginning with a valid command.
     * Each command must have any required arguments follow it.
     * Further commands may foloow the last argument of the first command, and will be interpreted in the same fashion as the first command.
     * Any number of command / argument combintaitons may be concatinated in this manner.
     * e.g. forward 10 rotate 90 forward 10 rotate -90 forward 20, is interpreted as three commands, each with a single argument.
     *
     * @param turtleState the state to apply the command(s) to.
     * @param line        the line of command(s) to interpret.
     * @throws IllegalArgumentException, Exception if commands, argument are invalid
     */
    public void run(TurtleState turtleState, String line) throws Exception {
        List<String> args = parseArgs(line);

        State state = createState(turtleState);
        while (!args.isEmpty()) {
            Command command = parseCommand(args);
            LOG.log(Level.INFO, String.format("\t'%s (%s)'\n", command.getClass().getSimpleName(), args.toString()));

            command.execute(state, args);
        }
    }

    private Command parseCommand(List<String> args) {
        String cmd = args.remove(0).toLowerCase();

        if (commands.containsKey(cmd)) {
            return commands.get(cmd);
        }

        if (macros.containsKey(cmd)) {
            List<String> macArgs = parseArgs(macros.get(cmd).toString(args));
            args.addAll(0, macArgs);
            return parseCommand(args);
        }

        if (patterns.containsKey(cmd)) {
            List<String> patArgs = parseArgs(patterns.get(cmd).toString(args));
            args.addAll(0, patArgs);
            return parseCommand(args);
        }
        throw new IllegalArgumentException(String.format("%s is an unknown command", cmd));
    }

    /**
     * parseLine parses the given line into a List of space seperated Strings, ignoring any spaces enclosed in quotes.
     *
     * @param line line to parse
     * @return List of seperated strings
     */
    private List<String> parseArgs(String line) {
        String cleanLine = cleanCodeBlocks(line);
        // https://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
        List<String> list = new ArrayList<>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cleanLine);
        while (m.find())
            list.add(m.group(1));
        return list;
    }

    // cleanCodeBlocks ensures delimiting [] brackets are spaced away from the enclosed commands so to act as an argument in themselves.
    private String cleanCodeBlocks(String cmdline) {
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        for (char ch : cmdline.toCharArray()) {
            if (ch == '\"') {
                inQuotes = !inQuotes;
            }
            String s = sb.toString();
            boolean lastCharToken = s.endsWith("[") || s.endsWith("]");
            boolean lastCharSpace = s.length() == 0 || Character.isWhitespace(s.charAt(s.length() - 1));
            boolean thisCharToken = ch == ']' || ch == '[';

            if (!inQuotes) {
                if ( (lastCharToken && !Character.isWhitespace(ch)) ||
                        (thisCharToken && !lastCharSpace) ){
                    sb.append(" ");
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }


    /**
     * findCodeBlock scans the given arguments for the items enclosed in '[' and ']' arguments.
     * The tokens must be in an argument on their own.
     *
     * @param args the arguments to scan
     * @return a subset of args enclosed in the code blocks, excluding the code block tokens.
     */
    public static List<String> findCodeBlock(List<String> args) {
        int openAt = args.indexOf("[");
        if (openAt < 0)
            return Collections.emptyList();

        // Scan args after opener, skipping past any inner blocks
        int closeAt = -1;
        int innerCount = 0;
        for (int i = openAt + 1; i < args.size(); i++) {
            String arg = args.get(i);
            if (arg.equals("[")) {
                innerCount++;
                continue;
            }
            if (arg.equals("]")) {
                innerCount--;
                if (innerCount < 0) {
                    closeAt = i;
                    break;
                }
            }
        }
        return closeAt >= 0 ? new ArrayList<>(args.subList(openAt, closeAt + 1)) : Collections.emptyList();
    }


    private State createState(TurtleState turtleState) {
        StateMap stack = new StateMap();
        stack.put("turtle", turtleState);
        stack.put("runtime", this);
        stack.put("env", System.getenv());
        return stack;
    }

    private static void buildCommands() {
        commands.clear();
        commands.put("forward", new ForwardCommand());
        commands.put("rotate", new RotateCommand());
        commands.put("pen", new PenCommand());
        commands.put("repeat", new RepeatCommand());
        commands.put("pattern", new PatternCommand());
        commands.put("home", new HomeCommand());
        commands.put("clean", new CleanCommand());
        commands.put("turtle", new TurtleVisibleCommand());
        commands.put("undo", new UndoCommand());
        commands.put("dump", new DumpCommand());
    }

    // macros are a list of alternative text, which is replaced in the command line, with its mapped value.
    private static void buildMacros() {
        macros.clear();
        macros.put("fd", new Macro("forward"));
        macros.put("back", new Macro("forward -{}"));
        macros.put("bk", new Macro("forward -{}"));
        macros.put("right", new Macro("rotate"));
        macros.put("rt", new Macro("rotate"));
        macros.put("left", new Macro("rotate -{}"));
        macros.put("lt", new Macro("rotate -{}"));

        macros.put("go", new Macro("forward"));
        macros.put("turn", new Macro("rotate"));
        macros.put("turn around", new Macro("rotate 180"));
        macros.put("turn back", new Macro("rotate 180"));

        macros.put("cleanscreen", new Macro("home clean"));
        macros.put("cs", new Macro("cleanscreen"));
        macros.put("hm", new Macro("home"));
        macros.put("cn", new Macro("clean"));

        macros.put("showturtle", new Macro("turtle true"));
        macros.put("st", new Macro("turtle true"));
        macros.put("hideturtle", new Macro("turtle false"));
        macros.put("ht", new Macro("turtle false"));

        macros.put("penup", new Macro("pen off"));
        macros.put("pendown", new Macro("pen on"));
        macros.put("pu", new Macro("pen off"));
        macros.put("pd", new Macro("pen on"));
        macros.put("draw", new Macro("pen on"));

        macros.put("rp", new Macro("repeat"));
        macros.put("pt", new Macro("pattern"));
        macros.put("ud", new Macro("undo"));
    }


}
