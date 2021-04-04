package org.spoofer.gui;

import javax.swing.*;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {

    public static final String MENU_FILE_OPEN = "file_open";
    public static final String MENU_FILE_SAVE = "file_save";
    public static final String MENU_FILE_EXPORT = "file_export";
    public static final String MENU_FILE_EXIT = "file_exit";
    public static final String MENU_VIEW_STATUS_BAR = "view_status_bar";
    public static final String MENU_VIEW_COMMAND = "view_command";
    public static final String MENU_VIEW_CONSOLE = "view_console";
    public static final String MENU_VIEW_TURTLE = "view_turtle";

    private static final String BUTTON_COMMAND_FORWARD = "forward 10";
    private static final String BUTTON_COMMAND_LEFT = "left 90";
    private static final String BUTTON_COMMAND_RIGHT = "right 90";
    private static final String BUTTON_COMMAND_PEN = "pen %s";
    
    public MainMenu(ActionListener actionListener, ActionListener runListener) {
        this.add(buildFileMenu(actionListener));
        this.add(buildViewMenu(actionListener));
        this.add(buildButtonBar(runListener));
    }

    private JMenu buildFileMenu(ActionListener menuListener) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileOpen = new JMenuItem("Open");
        fileOpen.setActionCommand(MENU_FILE_OPEN);
        fileOpen.setMnemonic(KeyEvent.VK_O);
        fileOpen.addActionListener(menuListener);
        fileMenu.add(fileOpen);

        JMenuItem fileSave = new JMenuItem("Save");
        fileSave.setMnemonic(KeyEvent.VK_S);
        fileSave.setActionCommand(MENU_FILE_SAVE);
        fileSave.addActionListener(menuListener);
        fileMenu.add(fileSave);

        fileMenu.addSeparator();
        
        JMenuItem fileExport = new JMenuItem("Export");
        fileExport.setMnemonic(KeyEvent.VK_X);
        fileExport.setActionCommand(MENU_FILE_EXPORT);
        fileExport.addActionListener(menuListener);
        fileMenu.add(fileExport);
        
        fileMenu.addSeparator();
        
        JMenuItem fileExit = new JMenuItem("Exit");
        fileExit.setMnemonic(KeyEvent.VK_E);
        fileExit.setActionCommand(MENU_FILE_EXIT);
        fileExit.addActionListener(menuListener);
        fileMenu.add(fileExit);

        return fileMenu;
    }

    private JMenu buildViewMenu(ActionListener menuListener) {
        JMenu menuView = new JMenu("View");
        menuView.setMnemonic(KeyEvent.VK_V);

        JCheckBoxMenuItem viewStatus = new JCheckBoxMenuItem("Status Bar");
        viewStatus.setActionCommand(MENU_VIEW_STATUS_BAR);
        viewStatus.addActionListener(menuListener);
        menuView.add(viewStatus);

        final JCheckBoxMenuItem viewCommand = new JCheckBoxMenuItem("Command Window");
        viewCommand.setActionCommand(MENU_VIEW_COMMAND);
        viewCommand.addActionListener(menuListener);
        menuView.add(viewCommand);

        final JCheckBoxMenuItem viewLog = new JCheckBoxMenuItem("Console");
        viewLog.setActionCommand(MENU_VIEW_CONSOLE);
        viewLog.addActionListener(menuListener);
        menuView.add(viewLog);

        menuView.addSeparator();
        final JCheckBoxMenuItem viewTutrle = new JCheckBoxMenuItem("Turtle");
        viewTutrle.setActionCommand(MENU_VIEW_TURTLE);
        viewTutrle.addActionListener(menuListener);
        menuView.add(viewTutrle);

        return menuView;
    }

    private JPanel buildButtonBar(ActionListener runListener) {
        JButton btnLeft = new JButton("Left");
        btnLeft.setActionCommand(BUTTON_COMMAND_LEFT);
        btnLeft.addActionListener(runListener);

        JButton btnForward = new JButton("Forward");
        btnForward.setActionCommand(BUTTON_COMMAND_FORWARD);
        btnForward.addActionListener(runListener);

        JButton btnRight = new JButton("Right");
        btnRight.setActionCommand(BUTTON_COMMAND_RIGHT);
        btnRight.addActionListener(runListener);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttons.add(btnLeft);
        buttons.add(btnForward);
        buttons.add(btnRight);

        JCheckBoxMenuItem penCheck = new JCheckBoxMenuItem("Draw");
        penCheck.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                runListener.actionPerformed(new ActionEvent(penCheck, 2, String.format(BUTTON_COMMAND_PEN, penCheck.isSelected())));
            }
        });
        buttons.add(new JLabel(""));
        buttons.add(penCheck);

        return buttons;
    }

}
