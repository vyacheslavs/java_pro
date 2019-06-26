package Lesson_6.TradingShips;

import java.util.ArrayList;

enum GoodTypes {
    CLOTH("Cloth"),
    FOOD("Food"),
    GAS("Gasoline");

    private String type;
    GoodTypes(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}

public class Ship implements Runnable {

    private GoodTypes goodType;
    private ArrayList<Stage> stagesCycle;
    private int canCarry = 500;
    private int carries = 0;
    private int number;

    Ship(int no, GoodTypes type, ArrayList<Stage> stages) {
        goodType = type;
        stagesCycle = stages;
        number = no;
    }

    @Override
    public void run() {
        boolean stop = false;
        while (!stop) {
            for(Stage s : stagesCycle) {
                System.out.println("Ship #"+number+" entering stage ["+s.getDescription()+"]");
                stop = !s.run(this);
                if (stop)
                    break;
            }
        }
        System.out.println("Ship #"+number+" has finished its work... ");
    }

    public int getCanCarry() {
        return canCarry;
    }

    public int getCarries() {
        return carries;
    }

    public GoodTypes getGoodType() {
        return goodType;
    }

    public int put(int v) {
        if (v + carries > canCarry)
            return 0;

        carries += v;
        return v;
    }

    public int get(int v) {
        if (carries - v <0)
            return 0;
        carries-=v;
        return v;
    }

    public int getNo() {
        return number;
    }
}
