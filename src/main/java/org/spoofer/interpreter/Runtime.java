package org.spoofer.interpreter;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * maintains a single thread to dequeue command queue of of processes and eexecute them.
 */
public class Runtime {

    private final Interpreter interpreter = new Interpreter();

    private final BlockingDeque<Process> commandQueue = new LinkedBlockingDeque<>();
    private final AtomicBoolean running = new AtomicBoolean(true);

    public boolean debug = false;

    public Runtime() {
        start();
    }

    public void shutdown() {
        running.set(false);
    }

    public Interpreter getInterpreter() {
        return interpreter;
    }

    public void run(Process proc) {
        try {
            commandQueue.put(proc);

        } catch (InterruptedException e) {
            throw new IllegalStateException("previous execution has taken too long. command '%s' failed.");
        }
    }

    public void start() {
        new Thread(() -> {
            if (debug)
                System.out.println("starting execution engine");

            while (running.get()) {
                Process proc;
                try {
                    proc = commandQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    continue;
                }

                if (debug)
                    System.out.printf("executing '%s'\n", proc.toString());

                try {
                    proc.getState().put("runtime", interpreter);
                    interpreter.run(proc);

                } catch (Exception e) {
                    if (debug)
                        e.printStackTrace();
                    else
                        System.err.println(e.getMessage());
                }

                proc.notifyComplete();
                if (debug) {
                    System.out.println("process complete");
                }
            }

            if (debug)
                System.out.println("stopping execution engine");
        }).start();
    }
}
