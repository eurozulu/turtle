package org.spoofer;

import org.spoofer.gui.MainMenu;
import org.spoofer.gui.MainPanel;
import org.spoofer.interpreter.Process;
import org.spoofer.interpreter.Runtime;
import org.spoofer.interpreter.State;
import org.spoofer.interpreter.StateMap;
import org.spoofer.misc.FileTools;
import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Turtle {

    private final TurtleState turtleState = new TurtleState();
    private State state = new StateMap();

    private final Runtime runtime = new Runtime();

    private final JFrame rootWindow = new JFrame("Turtle");

    public static void main(String[] args) {
        new Turtle().start();
    }

    private MainPanel mainPanel;
    private MainMenu mainMenu;


    public void start() {

        state.put("turtle", turtleState);

        mainPanel = new MainPanel(turtleState, runCommandListener);

        mainMenu = new MainMenu(menuListener, runCommandListener);

        rootWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        rootWindow.setJMenuBar(mainMenu);
        rootWindow.getContentPane().add(mainPanel, BorderLayout.CENTER);
        registerKeyEvents(rootWindow.getRootPane());

        rootWindow.pack();
        rootWindow.setVisible(true);
        refreshGui();
    }

    /**
     * register to listen for arrow key events
     *
     * @param rootPane root pane to receive events
     */
    private void registerKeyEvents(JRootPane rootPane) {
        InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = rootPane.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), MainMenu.BUTTON_COMMAND_FORWARD);
        actionMap.put(MainMenu.BUTTON_COMMAND_FORWARD, new KeyActionCommand(MainMenu.BUTTON_COMMAND_FORWARD));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), MainMenu.BUTTON_COMMAND_BACK);
        actionMap.put(MainMenu.BUTTON_COMMAND_BACK, new KeyActionCommand(MainMenu.BUTTON_COMMAND_BACK));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), MainMenu.BUTTON_COMMAND_LEFT);
        actionMap.put(MainMenu.BUTTON_COMMAND_LEFT, new KeyActionCommand(MainMenu.BUTTON_COMMAND_LEFT));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), MainMenu.BUTTON_COMMAND_RIGHT);
        actionMap.put(MainMenu.BUTTON_COMMAND_RIGHT, new KeyActionCommand(MainMenu.BUTTON_COMMAND_RIGHT));

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_HOME, 0), MainMenu.BUTTON_COMMAND_HOME);
        actionMap.put(MainMenu.BUTTON_COMMAND_HOME, new KeyActionCommand(MainMenu.BUTTON_COMMAND_HOME));
    }

    private ActionListener runCommandListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            runtime.run(new Process(state, cmd, () -> guiRefresher.callRefreshGui()));

            // negative ID's are NOT added to command window. (Such as commands from the window itself)
            if (e.getID() >= 0)
                mainPanel.appendCommandText(cmd + "\n");

        }
    };

    /**
     * refresh the vaious components with the current turtle state
     */
    private void refreshGui() {
        mainPanel.updateState(turtleState);
        mainMenu.updateMenu(turtleState, mainPanel.getCommandText().trim().length() > 0);
        rootWindow.repaint();
    }

    /**
     * Menulistener catches events from menu events and process the action
     */
    private final ActionListener menuListener = e -> {
        switch (e.getActionCommand()) {
            case MainMenu.MENU_FILE_EXIT:
                runtime.shutdown();
                System.exit(0);

            case MainMenu.MENU_FILE_OPEN:
                String s = FileTools.openFile(mainPanel.getCommandText().trim().length() > 0);
                if (s != null) {
                    mainPanel.setCommandText(s);
                }
                break;

            case MainMenu.MENU_FILE_SAVE: {
                String data = mainPanel.getCommandText();
                if (data.trim().length() == 0) {
                    JOptionPane.showConfirmDialog(null,
                            "Warning. The command window is empty",
                            "There is nothing to save!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String path = FileTools.requestSaveFilePath();
                if (path.equals(""))
                    return;
                try {
                    FileTools.saveFile(path, data);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                break;
            }

            case MainMenu.MENU_FILE_EXPORT: {
                if (turtleState.getTurtlePath().length <= 1) {
                    JOptionPane.showConfirmDialog(null,
                            "Warning. The turtle path is empty",
                            "There is nothing to export!",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String format = ((JMenuItem) e.getSource()).getName();
                String path = FileTools.requestSaveFilePath();
                try {
                    mainPanel.exportPath(path, format);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            break;

            case MainMenu.MENU_VIEW_TURTLE:
                turtleState.isTurtleVisible = ((JCheckBoxMenuItem) e.getSource()).isSelected();
                rootWindow.repaint();
                break;

            case MainMenu.MENU_DRAW_PAINT:
                TurtlePosition position = turtleState.getTurtlePosition();
                Color newColor = JColorChooser.showDialog(rootWindow, "Choose a color", position.paintColour);
                if (newColor != null) {
                    position.paintColour = newColor;
                    mainPanel.appendCommandText(String.format("colour #%s\n", Integer.toHexString(newColor.getRGB() & 0xFFFFFF)));
                }
                refreshGui();
                break;

            case MainMenu.MENU_COMMAND_RUN_ALL:
                String cmd = mainPanel.getCommandText().trim();
                runCommandListener.actionPerformed(new ActionEvent(e.getSource(), -1, cmd));
                break;
        }
    };


    /**
     * {@link KeyActionCommand} captures key events and passes them onto the runHandler
     */
    private class KeyActionCommand extends AbstractAction {
        public KeyActionCommand(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String name = this.getValue(Action.NAME).toString();
            runCommandListener.actionPerformed(new ActionEvent(e.getSource(), e.getID(), name));
        }
    }

    private final GuiRefresher guiRefresher = new GuiRefresher();

    class GuiRefresher implements Runnable {
        @Override
        public void run() {
            refreshGui();
        }

        public void callRefreshGui() {
            SwingUtilities.invokeLater(this);
        }
    }

}
