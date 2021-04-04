package org.spoofer;

import org.spoofer.gui.MainMenu;
import org.spoofer.gui.MainPanel;
import org.spoofer.interpreter.Interpreter;
import org.spoofer.model.TurtleState;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Turtle {

    private final Interpreter interpreter = new Interpreter();
    private final TurtleState turtleState = new TurtleState();

    private final JFrame rootWindow = new JFrame("Turtle");

    public static void main(String[] args) {
        new Turtle().start();
    }

    private MainPanel mainPanel;
    private MainMenu mainMenu;

    public void start() {
        Logger.getGlobal().setLevel(Level.WARNING);

        mainPanel = new MainPanel(turtleState, runCommandListener);
        mainMenu = new MainMenu(menuListener, runCommandListener);

        rootWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        rootWindow.setJMenuBar(mainMenu);
        rootWindow.getContentPane().add(mainPanel, BorderLayout.CENTER);
        rootWindow.pack();
        rootWindow.setVisible(true);
    }


    private ActionListener runCommandListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String cmd = e.getActionCommand();
                interpreter.run(turtleState, cmd);
                if (e.getID() >= 0)
                    mainPanel.appendCommandText(cmd + "\n");

            } catch (Exception err) {
                err.printStackTrace();
            }
            mainPanel.updateState(turtleState);
            rootWindow.repaint();
        }
    };

    /**
     * Menulistener catches events from menu events and process the action
     */
    private final ActionListener menuListener = e -> {
        switch (e.getActionCommand()) {
            case MainMenu.MENU_FILE_EXIT:
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
                if (path == "")
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
                String format = ((JMenuItem)e.getSource()).getName();
                String path = FileTools.requestSaveFilePath();
                try {
                    mainPanel.exportPath(path, format);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            break;

            case MainMenu.MENU_VIEW_TURTLE:
                turtleState.isVisible = ((JCheckBoxMenuItem)e.getSource()).isSelected();
                rootWindow.repaint();
                break;

            case MainMenu.MENU_COMMAND_RUN_ALL:
                String cmd = mainPanel.getCommandText().trim().replace("\n", " ");
                runCommandListener.actionPerformed(new ActionEvent(e.getSource(), -1, cmd));
                break;
        }
    };



}
