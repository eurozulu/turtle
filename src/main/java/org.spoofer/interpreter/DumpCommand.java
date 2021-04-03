package org.spoofer.interpreter;

import org.spoofer.model.TurtlePosition;

import java.awt.*;
import java.io.*;
import java.util.List;

public class DumpCommand implements Command {
    @Override
    public void execute(Stack stack, List<String> args) throws Exception {
        FileOutputStream fOut = null;
        if (!args.isEmpty()) {
            try {
                fOut = new FileOutputStream(args.get(0));
                args.remove(0);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("file location could not be opened ", e);
            }
        }

        OutputStreamWriter out = fOut != null ? new OutputStreamWriter(fOut) : new OutputStreamWriter(System.out);
        TurtlePosition[] path = stack.get("turtle.turtlePath");

        try {
            Dimension constraint = stack.get("turtle.contraint");
            out.write(String.format("Area: width=%d,height=%d\n", constraint.width,constraint.height ));
            for (TurtlePosition pos : path) {
                out.write(pos.toString());
                out.write('\n');
            }
            out.flush();

            if (fOut != null) {
                fOut.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("failed to write path ", e);
        }
    }
}
