package org.spoofer.gui;

import org.spoofer.model.TurtleState;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class MainPanel extends JPanel {

    private final JTextArea commands = new JTextArea();
    private final JTextPane console = new JTextPane();
    private final StatusBar statusBar = new StatusBar();
    private final TurtlePanel turtlePanel;


    public MainPanel(TurtleState turtleState, ActionListener runListener) {
        super(new BorderLayout());
        turtlePanel = new TurtlePanel(turtleState);

        captureOutputStreams();

        buildMainPanel(runListener);

        statusBar.update(turtleState);
    }

    public void setCommandText(String s) {
        commands.setText(s);
    }

    public void appendCommandText(String s) {
        commands.append(s);
    }

    public String getCommandText() {
        return commands.getText();
    }

    public void exportPath(String path, String format) throws IOException {
        turtlePanel.exportPath(path, format);
    }

    public void updateState(TurtleState turtleState) {
        statusBar.update(turtleState);
    }

    public String getLastLine() {
        int pos = commands.getCaretPosition() - 1;
        String[] lines = commands.getText().split("\n", -1);

        int lineNumber = 0;
        int len = 0;
        while (lineNumber < lines.length) {
            len += lines[lineNumber].length();
            if (len >= pos)
                break;
            lineNumber++;
            len++;
        }
        if (lineNumber >= lines.length)
            lineNumber = lines.length > 0 ? lines.length - 1 : 0;
        return lines[lineNumber];
    }

    private void buildMainPanel(ActionListener runListener) {

        commands.setColumns(50);
        commands.setEditable(true);

        commands.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_ENTER:
                        String line = getLastLine().trim();
                        if (line.length() > 0)
                            runListener.actionPerformed(new ActionEvent(commands, -1, line));
                }
            }
        });

        JPopupMenu commandPopup = new JPopupMenu("Command");
        commands.add(commandPopup);
        JMenuItem clearCmdMenu = new JMenuItem("Clear");
        clearCmdMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                commands.setText("");
            }
        });
        commandPopup.add(clearCmdMenu);
        commands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    commandPopup.show(commands, e.getX(), e.getY());
                }
            }
        });



        console.setPreferredSize(new Dimension(200, 200));
        console.setEditable(false);
        console.setBackground(Color.LIGHT_GRAY);

        JPopupMenu consolePopup = new JPopupMenu("Console");
        console.add(consolePopup);
        JMenuItem clearMenu = new JMenuItem("Clear");
        clearMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                console.setText("");
            }
        });
        consolePopup.add(clearMenu);
        console.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    consolePopup.show(console, e.getX(), e.getY());
                }
            }
        });

        JScrollPane cmdScroll = new JScrollPane(commands,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JScrollPane conScroll = new JScrollPane(console,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        JPanel lowerPanel = new JPanel(new BorderLayout());
        lowerPanel.add(conScroll, BorderLayout.CENTER);
        lowerPanel.add(statusBar, BorderLayout.SOUTH);

        this.add(turtlePanel, BorderLayout.CENTER);
        this.add(cmdScroll, BorderLayout.EAST);
        this.add(lowerPanel, BorderLayout.SOUTH);
    }

    private void captureOutputStreams() {
        StreamMultiplex out = new StreamMultiplex(new OutputStream[]{
                System.out,
                new CapturedOutputStream(Color.white)
        });
        System.setOut(new PrintStream(out));

        StreamMultiplex err = new StreamMultiplex(new OutputStream[]{
                System.err,
                new CapturedOutputStream(Color.red)
        });
        System.setErr(new PrintStream(err));
    }


    /**
     * Simple outputstream class to write the stream into the console in a predefined colour
     */
    private class CapturedOutputStream extends OutputStream {
        final ByteBuffer buff = ByteBuffer.allocate(1024);
        final SimpleAttributeSet attributeSet;

        public CapturedOutputStream(Color color) {
            attributeSet = new SimpleAttributeSet();
            StyleConstants.setForeground(attributeSet, color);
        }

        @Override
        public void write(int b) throws IOException {
            buff.put((byte) b);
            if (buff.position() > 50 || b == '\n') {
                buff.flip();
                writeToConsole(StandardCharsets.UTF_8.decode(buff).toString());
                buff.clear();
            }
        }

        private void writeToConsole(String s) {
            StyledDocument doc = console.getStyledDocument();
            try {
                doc.insertString(doc.getLength(), s, attributeSet);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }
}
