package org.spoofer.misc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.CharBuffer;

/**
 * Simple Helper functions for writing strings in and out of files
 */
public class FileTools {

    public static String openFile(boolean warnOverwrite) {
        if (warnOverwrite) {
            if (JOptionPane.showConfirmDialog(null,
                    "Warning. This will overwrite the existing command window",
                    "Are you sure you wish to discard the existing command window?",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION)
                return null;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setFileFilter(new FileNameExtensionFilter("Logo files", "logo"));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        try {
            return FileTools.readFile(fileChooser.getSelectedFile());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String requestSaveFilePath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        int result = fileChooser.showSaveDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return "";
        }
        return fileChooser.getSelectedFile().getAbsolutePath();
    }


    public static String readFile(File f) throws IOException {
        return readFile(new FileInputStream(f));
    }

    public static String readFile(String path) throws IOException {
        return readFile(new FileInputStream(path));
    }

    public static String readFile(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();
        CharBuffer buffer = CharBuffer.allocate(1024);
        InputStreamReader in = new InputStreamReader(inputStream);

        while ((in.read(buffer)) > 0) {
            buffer.flip();
            sb.append(buffer.asReadOnlyBuffer());
            buffer.clear();
        }
        in.close();
        return sb.toString();
    }

    public static void saveFile(String path, String data) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(path));
        out.write(data);
        out.flush();
        out.close();
    }

    public static void saveFile(String path, byte[] data) throws IOException {
        FileOutputStream out = new FileOutputStream(path);
        out.write(data);
        out.flush();
        out.close();
    }


    public static BufferedImage loadImage(String name, ClassLoader cloader) {
        InputStream inputStream = cloader.getResourceAsStream(String.format("%s.png", name));
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
