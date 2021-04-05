package org.spoofer.commands;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.interpreter.State;
import org.spoofer.model.TurtlePosition;

import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.Map;

public class DumpCommand implements Command {
    @Override
    public void execute(State state, List<String> args) throws Exception {
        FileOutputStream fOut = null;
        if (!args.isEmpty()) {
            try {
                fOut = new FileOutputStream(args.remove(0));
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("file location could not be opened ", e);
            }
        }

        OutputStreamWriter out = fOut != null ? new OutputStreamWriter(fOut) : new OutputStreamWriter(System.out);
        TurtlePosition[] path = state.get("turtle.turtlePath");
        Dimension constraint = state.get("turtle.contraint");
        Map<String, String> patterns = state.get("patterns");

        try {
            out.write("{\n");
            out.write(String.format("\"area\": { \"width\": %d, \n\"height\": %d}", constraint.width, constraint.height));
            out.write(",\n\"path\": [");
            boolean first = true;
            for (TurtlePosition pos : path) {
                if (!first) {
                    out.write(",");
                } else {
                    first = false;
                }
                out.write(String.format("{ \"x\": %d, \"y\": %d, \"rotation\": %d, \"pen\": %s }",
                        pos.x, pos.y, Math.round(pos.rotation), pos.imprinted) );
            }
            out.write("]");


            out.write(",\n\"patterns\": [");
            first = true;
            for (Map.Entry<String, String> e : patterns.entrySet()) {
                if (!first) {
                    out.write(",");
                } else {
                    first = false;
                }
                out.write(String.format("{ \"%s\": \"%s\"}", e.getKey(), e.getValue()) );
            }
            out.write("]\n");

            out.write('}');
            out.flush();

            if (fOut != null) {
                fOut.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("failed to write path ", e);
        }
    }
}
