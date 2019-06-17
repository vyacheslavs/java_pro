package Lesson_3;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileSewing {

    public static void main(String[] args) {

        try {
            File targetClassesDir = new File(FileSewing.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            ArrayList<InputStream> inputSources = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                inputSources.add(new FileInputStream(targetClassesDir+"/Lesson_3/filesew_"+i+".xml"));
            }

            try (FileOutputStream fout = new FileOutputStream(targetClassesDir+"/Lesson_3/sequence.xml")) {
                try (SequenceInputStream sequenceInputStream = new SequenceInputStream(Collections.enumeration(inputSources))) {
                    byte[] buffer = new byte[10*1024];

                    for (int length; (length = sequenceInputStream.read(buffer)) != -1; ){
                        fout.write(buffer, 0, length);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
