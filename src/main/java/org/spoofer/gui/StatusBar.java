package org.spoofer.gui;

import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import javax.swing.*;
import java.awt.*;

public class StatusBar extends JPanel {

    private final JTextField statusPosition = new JTextField();
    private final JTextField statusRotation = new JTextField();
    private final JTextField statusPen = new JTextField();
    private final JPanel statusColour = new JPanel();
    private final JTextField statusConstraint = new JTextField();

    public StatusBar() {
        super((new FlowLayout(FlowLayout.RIGHT)));
        buildStatusBar();
    }

    public void update(TurtleState turtleState) {
        Dimension constaints = turtleState.getContraint();
        statusConstraint.setText(String.format("width: %d\theight: %d", constaints.width, constaints.height));
        TurtlePosition position = turtleState.getTurtlePosition();
        statusPosition.setText(String.format("x: %d\ty: %d", position.x, position.y));
        statusRotation.setText(String.format("%3d", Math.round(position.rotation)));
        statusPen.setText(position.imprinted ? "On" : "Off");
        statusColour.setBackground(position.paintColour);
    }

    private void buildStatusBar() {
        statusConstraint.setEditable(false);
        statusConstraint.setColumns(15);
        this.add(new JLabel("Limits: "));
        this.add(statusConstraint);

        statusPosition.setEditable(false);
        statusPosition.setColumns(15);
        this.add(new JLabel("Postion: "));
        this.add(statusPosition);

        statusRotation.setEditable(false);
        statusRotation.setColumns(6);
        this.add(new JLabel("Rotation: "));
        this.add(statusRotation);

        statusPen.setEditable(false);
        statusPen.setColumns(4);
        this.add(new JLabel("Pen: "));
        this.add(statusPen);

        statusColour.setPreferredSize(new Dimension(10,10));
        this.add(new JLabel("Colour: "));
        this.add(statusColour);
    }


}
