package Lesson_3;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ByteReader {

    public static void main(String[] args) {
        try {
            File targetClassesDir = new File(ByteReader.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            try (FileInputStream in_stream = new FileInputStream(targetClassesDir+"/Lesson_3/bytereader.data")) {
                int x;
                while ((x = in_stream.read())!=-1) {
                    char c = (char)x;
                    byte b = (byte)x;

                    System.out.println(String.format("'%c' - 0x%02x", c, b));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
