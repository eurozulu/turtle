package org.spoofer.gui;

import org.spoofer.misc.FileTools;
import org.spoofer.model.TurtlePosition;
import org.spoofer.model.TurtleState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link TurtlePanel} paints the Turtle and its path into the panel background.
 */
public class TurtlePanel extends JPanel {

    private static final Color PATH_COLOUR = Color.BLUE;

    private final TurtleState turtleState;

    private final BufferedImage turtleIcon;

    public TurtlePanel(TurtleState turtleState) {
        super();
        turtleIcon = FileTools.loadImage("turtle", getClass().getClassLoader());
        this.turtleState = turtleState;
    }

    public void exportPath(String path, String format) throws IOException {
        // https://stackoverflow.com/questions/5655908/export-jpanel-graphics-to-png-or-gif-or-jpg/5656017
        String ext = "." + format.toLowerCase();
        if (!path.toLowerCase().endsWith(ext))
            path = path + ext;

        boolean isVis = turtleState.isTurtleVisible;
        turtleState.isTurtleVisible = false;
        BufferedImage bi = new BufferedImage(this.getSize().width, this.getSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        this.paint(g);
        g.dispose();
        ImageIO.write(bi, format, new File(path));
        turtleState.isTurtleVisible = isVis;
    }

    @Override
    protected void paintComponent(Graphics g) {
        paintPath(g);
        if (turtleState.isTurtleVisible)
            paintTurtle(g);
    }

    private void paintGrid(Graphics g) {

    }

    private void paintPath(Graphics g) {
        g.setColor(PATH_COLOUR);

        Rectangle bounds = g.getClipBounds();
        TurtlePosition prev = null;
        for (TurtlePosition pos : turtleState.getTurtlePath()) {
            if (prev != null && prev.imprinted) {
                Point tPos = getTurtlePoint(pos.x, pos.y, bounds);
                Point tPosPrev = getTurtlePoint(prev.x, prev.y, bounds);
                g.drawLine(tPosPrev.x, tPosPrev.y, tPos.x, tPos.y);
            }
            prev = pos;
        }
    }

    private void paintTurtle(Graphics g) {
        TurtlePosition position = turtleState.getTurtlePosition();
        BufferedImage bi = rotate(turtleIcon, Math.toRadians(position.rotation));
        Point tpos = getTurtlePoint(position.x, position.y, g.getClipBounds());
        g.drawImage(bi, tpos.x - (bi.getWidth() / 2), tpos.y - (bi.getHeight() / 2), null);
    }

    private Point getTurtlePoint(int x, int y, Rectangle bounds) {
        double unitX = ((double)bounds.width / (double)turtleState.getContraint().width);
        double unitY = ((double)bounds.height / (double)turtleState.getContraint().height);
        long turtleX = Math.round(x * unitX);
        long turtleY = Math.round(y * unitY);
        if (turtleX > Integer.MAX_VALUE || turtleY > Integer.MAX_VALUE)
            throw new IllegalStateException("Position invalid, outsides expected bounds");
        return new Point((int)turtleX, (int)turtleY);
    }


    public BufferedImage rotate(BufferedImage image, double angle) {
        // https://stackoverflow.com/questions/4156518/rotate-an-image-in-java
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2f, h / 2f);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }
}
