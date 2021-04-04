package org.spoofer;

import java.io.*;
import java.nio.CharBuffer;

public class FileTools {



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
}
