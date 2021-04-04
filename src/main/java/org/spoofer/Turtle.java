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

    public void start() {
        Logger.getGlobal().setLevel(Level.FINE);

        rootWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        rootWindow.setExtendedState(JFrame.MAXIMIZED_BOTH);

        mainPanel = new MainPanel(turtleState, runCommandListener);
        rootWindow.setJMenuBar(new MainMenu(menuListener, runCommandListener));

        rootWindow.getContentPane().add(mainPanel, BorderLayout.CENTER);
        rootWindow.pack();
        rootWindow.setVisible(true);
    }


    private ActionListener runCommandListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                interpreter.run(turtleState, e.getActionCommand());
            } catch (Exception err) {
                System.err.println(String.format("ERROR: %s", err.getMessage()));
            }
            mainPanel.updateState(turtleState);
            rootWindow.repaint();
        }
    };

    private final ActionListener menuListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case MainMenu.MENU_FILE_EXIT:
                    System.exit(0);

                case MainMenu.MENU_FILE_OPEN:
                    openFile();
                    break;

                case MainMenu.MENU_FILE_SAVE:
                    saveFile();
                    break;

                case MainMenu.MENU_VIEW_TURTLE:
                    turtleState.isVisible = ((JCheckBoxMenuItem)e.getSource()).isSelected();
                    rootWindow.repaint();
            }
        }
    };


    private void openFile() {
        if (mainPanel.getCommandText().trim().length() > 0) {
            if (JOptionPane.showConfirmDialog(null,
                    "Warning. This will overwrite the existing command window",
                    "Are you sure you wish to discard the existing command window?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Logo files", "logo"));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            String fileText = FileTools.readFile(fileChooser.getSelectedFile());
            mainPanel.setCommandText(fileText);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void saveFile() {

        String data = mainPanel.getCommandText();
        if (data.trim().length() == 0) {
            JOptionPane.showConfirmDialog(null,
                    "Warning. The command window is empty",
                    "There is nothing to save!",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String path = fileChooser.getSelectedFile().getAbsolutePath();
        if (!path.endsWith(".logo")) {
            path += ".logo";
        }
        try {
            FileTools.saveFile(path, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
