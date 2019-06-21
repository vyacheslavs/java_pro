package Lesson_4;

/**
 *  Создать три потока, каждый из которых выводит определенную букву (A, B и C) 5 раз (порядок – ABСABСABС). Используйте wait/notify/notifyAll.
 */


public class Juggler {
    static volatile int counter = 1;
    static volatile int cycle = 0;
    static final Object obj = new Object();

    static class JugglerThread implements Runnable {

        private char letter;
        private int order;

        JugglerThread(char _letter, int _order) {
            this.letter = _letter;
            this.order = _order;
        }

        @Override
        public void run() {
            synchronized (obj) {
                while (cycle<3) {
                    if (counter == order) {
                        System.out.print(letter);
                        counter++;
                        if (counter>3) {
                            counter = 1;
                            cycle++;
                        }
                        obj.notifyAll();
                    } else {
                        try {
                            obj.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }


    public static void main(String[] args) {

        Thread th1 = new Thread(new JugglerThread('A', 1));
        Thread th2 = new Thread(new JugglerThread('B', 2));
        Thread th3 = new Thread(new JugglerThread('C', 3));

        th1.start();
        th2.start();
        th3.start();

        try {
            th1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            th2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            th3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
