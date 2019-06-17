package Lesson_3;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class PagedViewer {

    final static int PAGE_SIZE = 1800;

    public static void main(String[] args) {
        File targetClassesDir = new File(PagedViewer.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter page number (pages start with 0): ");
        int page = scanner.nextInt();

        try (RandomAccessFile raf = new RandomAccessFile(targetClassesDir+"/Lesson_3/t.txt", "r")) {
            byte[] bytes = new byte[PAGE_SIZE];
            raf.seek(page * PAGE_SIZE );
            int rd = raf.read(bytes, 0, PAGE_SIZE);
            if (rd>0) {
                System.out.println(new String(bytes, 0, rd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
