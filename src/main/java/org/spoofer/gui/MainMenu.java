package org.spoofer.gui;

import org.spoofer.model.TurtleState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class MainMenu extends JMenuBar {

    public static final String MENU_FILE_OPEN = "file_open";
    public static final String MENU_FILE_SAVE = "file_save";
    public static final String MENU_FILE_EXPORT = "file_export";
    public static final String MENU_FILE_EXIT = "file_exit";
    public static final String MENU_VIEW_TURTLE = "view_turtle";
    public static final String MENU_DRAW_PAINT = "draw_colour";
    public static final String MENU_COMMAND_RUN = "command_run";
    public static final String MENU_COMMAND_RUN_ALL = "command_runall";

    public static final String BUTTON_COMMAND_FORWARD = "forward 10";
    public static final String BUTTON_COMMAND_BACK = "back 10";
    public static final String BUTTON_COMMAND_LEFT = "left 90";
    public static final String BUTTON_COMMAND_RIGHT = "right 90";
    public static final String BUTTON_COMMAND_PEN = "pen %s";
    public static final String BUTTON_COMMAND_CLEAN = "clean";
    public static final String BUTTON_COMMAND_HOME = "home";

    // Menus with active state
    private final JMenuItem fileSave = new JMenuItem("Save");
    private final JMenu fileExport = new JMenu("Export");
    private final JCheckBoxMenuItem viewTurtle = new JCheckBoxMenuItem("Turtle");
    private final JCheckBoxMenuItem penState = new JCheckBoxMenuItem("Draw");
    private final JButton btnClean = new JButton("Clean");

    public MainMenu(ActionListener menuListener, ActionListener runListener) {
        this.add(buildFileMenu(menuListener));
        this.add(buildViewMenu(menuListener));
        buildButtonBar(menuListener, runListener);
    }

    public void updateMenu(TurtleState turtleState, boolean hasCommands) {
        fileSave.setEnabled(hasCommands);
        fileExport.setEnabled(hasCommands);
        viewTurtle.setSelected(turtleState.isTurtleVisible);
        penState.setSelected(turtleState.getTurtlePosition().imprinted);
        btnClean.setEnabled(!turtleState.IsEmpty());
    }

    private JMenu buildFileMenu(ActionListener menuListener) {
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem fileOpen = new JMenuItem("Open");
        fileOpen.setActionCommand(MENU_FILE_OPEN);
        fileOpen.setMnemonic(KeyEvent.VK_O);
        fileOpen.addActionListener(menuListener);
        fileMenu.add(fileOpen);

        fileSave.setMnemonic(KeyEvent.VK_S);
        fileSave.setActionCommand(MENU_FILE_SAVE);
        fileSave.addActionListener(menuListener);
        fileMenu.add(fileSave);

        fileMenu.addSeparator();

        fileExport.setMnemonic(KeyEvent.VK_X);
        JMenuItem exportPng = new JMenuItem("PNG");
        exportPng.setActionCommand(MENU_FILE_EXPORT);
        exportPng.setName("png");
        exportPng.addActionListener(menuListener);
        fileExport.add(exportPng);

        JMenuItem exportJpg = new JMenuItem("Jpeg");
        exportJpg.setActionCommand(MENU_FILE_EXPORT);
        exportJpg.setName("jpg");
        exportJpg.addActionListener(menuListener);
        fileExport.add(exportJpg);

        JMenuItem exportGif = new JMenuItem("GIF");
        exportGif.setActionCommand(MENU_FILE_EXPORT);
        exportGif.setName("gif");
        exportGif.addActionListener(menuListener);
        fileExport.add(exportGif);

        JMenuItem exportBmp = new JMenuItem("Bitmap");
        exportBmp.setActionCommand(MENU_FILE_EXPORT);
        exportGif.setName("bmp");
        exportBmp.addActionListener(menuListener);
        fileExport.add(exportBmp);

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

        menuView.addSeparator();
        viewTurtle.setActionCommand(MENU_VIEW_TURTLE);
        viewTurtle.addActionListener(menuListener);
        menuView.add(viewTurtle);

        return menuView;
    }

    private void buildButtonBar(ActionListener menuListener, ActionListener runListener) {
        JButton btnLeft = new JButton("Left");
        btnLeft.setActionCommand(BUTTON_COMMAND_LEFT);
        btnLeft.addActionListener(runListener);

        JButton btnForward = new JButton("Forward");
        btnForward.setActionCommand(BUTTON_COMMAND_FORWARD);
        btnForward.addActionListener(runListener);

        JButton btnBack = new JButton("Back");
        btnBack.setActionCommand(BUTTON_COMMAND_BACK);
        btnBack.addActionListener(runListener);

        JButton btnRight = new JButton("Right");
        btnRight.setActionCommand(BUTTON_COMMAND_RIGHT);
        btnRight.addActionListener(runListener);

        JPanel buttonsNav = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JPanel fwBkPanel = new JPanel(new BorderLayout());
        fwBkPanel.add(btnForward, BorderLayout.NORTH);
        fwBkPanel.add(btnBack, BorderLayout.SOUTH);

        buttonsNav.add(btnLeft);
        buttonsNav.add(fwBkPanel);
        buttonsNav.add(btnRight);

        penState.addActionListener(e -> runListener.actionPerformed(new ActionEvent(penState, 2, String.format(BUTTON_COMMAND_PEN, penState.isSelected()))));
        JPanel buttonsDraw = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        btnClean.addActionListener(e -> {
            runListener.actionPerformed(new ActionEvent(btnClean, 2, BUTTON_COMMAND_CLEAN));
        });
        JButton btnHome = new JButton("Home");
        btnHome.addActionListener(e -> {
            runListener.actionPerformed(new ActionEvent(btnClean, 2, BUTTON_COMMAND_HOME));
        });

        JButton btnColour = new JButton("Paint");
        btnColour.addActionListener(e -> {
            menuListener.actionPerformed(new ActionEvent(btnClean, 2, MENU_DRAW_PAINT));
        });
        JPanel penPanel = new JPanel(new BorderLayout());
        penPanel.add(penState, BorderLayout.NORTH);
        penPanel.add(btnColour, BorderLayout.SOUTH);

        buttonsDraw.add(penPanel);
        buttonsDraw.add(btnClean);
        buttonsDraw.add(btnHome);


        JButton btnRunAll = new JButton("Run All");
        btnRunAll.setActionCommand(MENU_COMMAND_RUN_ALL);
        btnRunAll.addActionListener(menuListener);

        JButton btnRunLine = new JButton("Run line");
        btnRunLine.setActionCommand(MENU_COMMAND_RUN);
        btnRunLine.addActionListener(menuListener);

        JPanel buttonsRun = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        buttonsRun.add(btnRunAll);
        buttonsRun.add(btnRunLine);

        //JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 5));
        this.add(buttonsNav);
        this.add(buttonsDraw);
        this.add(buttonsRun);

    }

}
