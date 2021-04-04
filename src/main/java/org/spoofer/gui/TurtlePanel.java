package org.spoofer.gui;

import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class TurtlePanel extends JPanel {

    private static final Color PATH_COLOUR = Color.BLUE;

    private final TurtleState turtleState;

    private final BufferedImage turtleIcon;


    public TurtlePanel(TurtleState turtleState) {
        super();
        turtleIcon = loadImage("turtle");
        this.turtleState = turtleState;
    }

    public void exportPath(String path, String format) throws IOException {
        // https://stackoverflow.com/questions/5655908/export-jpanel-graphics-to-png-or-gif-or-jpg/5656017
        String ext = "." + format.toLowerCase();
        if (!path.toLowerCase().endsWith(ext))
            path = path + ext;

        boolean isVis = turtleState.isVisible;
        turtleState.isVisible = false;
        BufferedImage bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        this.paint(g);
        g.dispose();
        ImageIO.write(bi, format, new File(path));
        turtleState.isVisible = isVis;
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintPath(g);
        paintTurtle(g);
    }


    private void paintPath(Graphics g) {
        int unitX = (g.getClipBounds().width / turtleState.getContraint().width);
        int unitY = (g.getClipBounds().height / turtleState.getContraint().height);

        g.setColor(PATH_COLOUR);

        TurtlePosition prev = null;
        for (TurtlePosition pos : turtleState.getTurtlePath()) {
            if (prev != null && pos.imprinted) {
                g.drawLine(prev.x * unitX, prev.y * unitY, pos.x * unitX, pos.y * unitY);
            }
            prev = pos;
        }
    }

    private void paintTurtle(Graphics g) {
        if (!turtleState.isVisible)
            return;

        int unitX = (g.getClipBounds().width / turtleState.getContraint().width);
        int unitY = (g.getClipBounds().height / turtleState.getContraint().height);
        TurtlePosition position = turtleState.getTurtlePosition();
        g.drawImage(turtleIcon, position.x * unitX, position.y * unitY, 35, 35, null);
    }

    protected BufferedImage loadImage(String name) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(String.format("%s.png", name));
        if (inputStream == null) {
            throw new IllegalStateException("image not found! " + name);
        }
        try {
            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load image  resource", e);
        }
    }
}
