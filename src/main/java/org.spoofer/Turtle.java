package org.spoofer;

import org.spoofer.interpreter.Interpreter;
import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;
import org.spoofer.gui.TurtlePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Turtle {
    private static final String BUTTON_COMMAND_FORWARD = "forward 10";
    private static final String BUTTON_COMMAND_LEFT = "rotate left";
    private static final String BUTTON_COMMAND_RIGHT = "rotate right";
    private static final String BUTTON_COMMAND_PEN = "pen %s";

    private final Interpreter interpreter = new Interpreter();
    private final TurtleState turtleState = new TurtleState();

    private final JFrame rootWindow = new JFrame("Turtle");
    private final JTextArea commandLog = new JTextArea();
    private final JTextField statusPosition = new JTextField();
    private final JTextField statusRotation = new JTextField();
    private final JTextField statusPen = new JTextField();
    private final JTextField statusConstraint = new JTextField();

    private final JPanel cmdLine = buildCommandLine();

    public static void main(String[] args) {
        new Turtle().start();
    }

    public void start() {
        Logger.getGlobal().setLevel(Level.FINE);

        rootWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        rootWindow.setJMenuBar(buildMenu());

        Container rootPanel = rootWindow.getContentPane();

        rootPanel.add(cmdLine, BorderLayout.NORTH);
        rootPanel.add(buildMainPanel(), BorderLayout.CENTER);
        rootPanel.add(buildStatusBar(), BorderLayout.SOUTH);

        rootWindow.pack();
        rootWindow.show();

    }

    private JMenuBar buildMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenu fileOpen = new JMenu("Open");
        fileOpen.setMnemonic(KeyEvent.VK_O);
        fileMenu.add(fileOpen);

        JMenu fileSave = new JMenu("Save");
        fileOpen.setMnemonic(KeyEvent.VK_S);
        fileMenu.add(fileSave);

        fileMenu.addSeparator();
        JMenu fileExport = new JMenu("Export");
        fileOpen.setMnemonic(KeyEvent.VK_X);
        fileMenu.add(fileExport);

        fileMenu.addSeparator();
        JMenu fileExit = new JMenu("Exit");
        fileOpen.setMnemonic(KeyEvent.VK_E);
        fileExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        fileMenu.add(fileExit);

        JMenu menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);
        menuBar.add(menuView);

        JCheckBoxMenuItem viewStatus = new JCheckBoxMenuItem("Status Bar");
        menuView.add(viewStatus);
        viewStatus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        final JCheckBoxMenuItem viewCommand = new JCheckBoxMenuItem("Command Line");
        menuView.add(viewCommand);
        viewCommand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cmdLine.setVisible(viewCommand.isSelected());
                cmdLine.getParent().invalidate();
            }
        });
        menuView.add(viewCommand);

        final JCheckBoxMenuItem viewLog = new JCheckBoxMenuItem("Log");
        menuView.add(viewLog);
        viewLog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Container parent = commandLog.getParent().getParent();
                parent.setVisible(viewLog.isSelected());
                parent.getParent().invalidate();
            }
        });
        menuView.add(viewLog);


        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton btnLeft = new JButton("Left");
        btnLeft.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCommand(BUTTON_COMMAND_LEFT);
            }
        });
        JButton btnForward = new JButton("Forward");
        btnForward.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCommand(BUTTON_COMMAND_FORWARD);
            }
        });
        JButton btnRight = new JButton("Right");
        btnRight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCommand(BUTTON_COMMAND_RIGHT);
            }
        });
        buttons.add(btnLeft);
        buttons.add(btnForward);
        buttons.add(btnRight);

        menuBar.add(buttons);
        JCheckBoxMenuItem penCheck = new JCheckBoxMenuItem("Draw");
        penCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runCommand(String.format(BUTTON_COMMAND_PEN, Boolean.toString(penCheck.isSelected())));
            }
        });
        buttons.add(new JLabel(""));
        buttons.add(penCheck);

        viewStatus.setSelected(true);
        viewCommand.setSelected(false);

        return menuBar;
    }

    private JPanel buildStatusBar() {
        JPanel toolbar = new JPanel(new BorderLayout());

        JPanel statusBar = new JPanel((new FlowLayout(FlowLayout.RIGHT)));
        statusBar.add(new JLabel("Limits: "));
        statusConstraint.setEditable(false);
        statusConstraint.setColumns(15);
        statusBar.add(statusConstraint);

        statusBar.add(new JLabel("Postion: "));
        statusPosition.setEditable(false);
        statusPosition.setColumns(15);
        statusBar.add(statusPosition);

        statusBar.add(new JLabel("Rotation: "));
        statusBar.add(statusRotation);
        statusRotation.setEditable(false);
        statusRotation.setColumns(6);

        statusBar.add(new JLabel("Pen: "));
        statusBar.add(statusPen);
        statusPen.setEditable(false);
        statusPen.setColumns(4);
        toolbar.add(statusBar, BorderLayout.SOUTH);
        return toolbar;
    }

    private JPanel buildMainPanel() {
        JPanel split = new JPanel(new BorderLayout());
        split.add(new TurtlePanel(turtleState), BorderLayout.CENTER);

        commandLog.setRows(20);
        commandLog.setEditable(false);
        commandLog.setBackground(Color.LIGHT_GRAY);
        JScrollPane logScroller = new JScrollPane(commandLog,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        split.add(logScroller, BorderLayout.SOUTH);
        logScroller.setVisible(false);
        return split;
    }

    private JPanel buildCommandLine() {
        JPanel cmdLinePanel = new JPanel(new BorderLayout());
        JLabel cmdLineLbl = new JLabel("Command: ");
        final JTextField cmdLine = new JTextField();
        ActionListener runCmdLine = e -> {
            runCommand(cmdLine.getText());
            cmdLine.setText("");
        };

        cmdLine.addActionListener(runCmdLine);
        JButton btnCmdLine = new JButton("Enter");
        btnCmdLine.addActionListener(runCmdLine);

        cmdLinePanel.add(cmdLineLbl, BorderLayout.WEST);
        cmdLinePanel.add(cmdLine, BorderLayout.CENTER);
        cmdLinePanel.add(btnCmdLine, BorderLayout.EAST);

        cmdLinePanel.setVisible(true);
        return cmdLinePanel;
    }


    private void runCommand(String cmd) {
        try {
            interpreter.run(turtleState, cmd);
        } catch (Exception e) {
            cmd = String.format("ERROR: %s", e.getMessage());
            System.err.println(cmd);
        }
        commandLog.setText(commandLog.getText() + "\n" + cmd);

        Dimension constaints = turtleState.getContraint();
        statusConstraint.setText(String.format("width: %d\theight: %d", constaints.width, constaints.height));
        TurtlePosition position = turtleState.getTurtlePosition();
        statusPosition.setText(String.format("x: %d\ty: %d", position.x, position.y));
        statusRotation.setText(String.format("%3d", Math.round(position.rotation)));
        statusPen.setText(position.imprinted ? "On" : "Off");
        statusPen.getParent().invalidate();
        rootWindow.repaint();
    }
}
