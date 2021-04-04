package org.spoofer.gui;

import java.io.IOException;
import java.io.OutputStream;

public class StreamMultiplex extends OutputStream {

    private final OutputStream[] outputs;

    public StreamMultiplex(OutputStream[] outputs) {
        if (outputs == null || outputs.length == 0)
            throw new IllegalArgumentException("No underlying streams");

        this.outputs = outputs;
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream out : outputs)
            out.write(b);
    }
}
