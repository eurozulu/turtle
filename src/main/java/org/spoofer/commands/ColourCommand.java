package org.spoofer.commands;

import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;

import java.awt.*;
import java.util.List;

public class ColourCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        if (args.isEmpty()) {
            throw new IllegalArgumentException("No colour value found.  Provide a colour, either as #rrggbb or 'red', 'green', 'blue', 'black'");
        }
        String arg = args.remove(0);

        Color color;
        try {
            color = parseColour(arg);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("No colour value found.  Provide a colour, either as #rrggbb or 'red', 'green', 'blue', 'black'");
        }
        TurtlePosition position = state.get("turtle.turtlePosition");
        position.paintColour = color;
    }

    public Color parseColour(String c) {
        switch (c.toLowerCase()) {
            case "red":
                return Color.red;
            case "blue":
                return Color.blue;
            case "green":
                return Color.green;
            case "yellow":
                return Color.yellow;
            case "black":
                return Color.black;
            case "orange":
                return Color.orange;
            case "pink":
                return Color.pink;
            case "cyan":
                return Color.cyan;
            default:
                if (c.startsWith("#")) {
                    return Color.decode(c);
                }
        }
        throw new NumberFormatException(String.format("%s is not a known colour", c));
    }
}
