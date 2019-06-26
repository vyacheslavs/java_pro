package Lesson_6.TradingShips;

import java.util.ArrayList;

public class Main {

    final static int MAX_SHIPS = 20;

    public static void main(String[] args) {

        ArrayList<Stage> stages = new ArrayList<>();
        stages.add(new LoadUnloadStage( true,
                new Pier(GoodTypes.CLOTH, 2700),
                new Pier(GoodTypes.FOOD, 5900),
                new Pier(GoodTypes.GAS, 8500)
        ));
        stages.add(new TransferStage(true));
        stages.add(new LoadUnloadStage(false,
                new Pier(GoodTypes.CLOTH, 0),
                new Pier(GoodTypes.FOOD, 0),
                new Pier(GoodTypes.GAS, 0)
        ));
        stages.add(new TransferStage(false));

        Thread[] threads = new Thread[MAX_SHIPS];
        Ship[] ships = new Ship[MAX_SHIPS];
        for (int i = 0; i < MAX_SHIPS; i++) {
            GoodTypes type = GoodTypes.CLOTH;
            if (i>MAX_SHIPS * 0.3)
                type = GoodTypes.FOOD;
            if (i>MAX_SHIPS * 0.6)
                type = GoodTypes.GAS;
            ships[i] = new Ship(i, type, stages);
            threads[i] = new Thread(ships[i]);
            threads[i].start();
        }

        for (int i = 0; i < MAX_SHIPS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
