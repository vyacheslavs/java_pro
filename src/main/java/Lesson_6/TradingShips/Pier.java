package Lesson_6.TradingShips;

public class Pier {
    private GoodTypes type;
    private int goodCount;
    private int loadingPerTurn = 100;

    Pier(GoodTypes type, int goodCount) {
        this.type = type;
        this.goodCount = goodCount;
    }

    public GoodTypes getType() {
        return type;
    }

    public synchronized boolean startLoading(Ship c) {

        if (goodCount==0)
            return false;

        System.out.println("Ship #"+c.getNo()+" is loading goods: "+c.getGoodType());

        for (int i = 0; i < c.getCanCarry(); i+=loadingPerTurn) {
            int ltp = loadingPerTurn;
            if (goodCount<ltp)
                ltp = goodCount;
            goodCount -= c.put(loadingPerTurn);
            System.out.println("Ship #"+c.getNo()+" now loaded "+c.getCarries()+" items of "+c.getGoodType()+", Pier leftover: "+goodCount);

            if (goodCount == 0)
                return true;

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public synchronized boolean startUnloading(Ship c) {
        System.out.println("Ship #"+c.getNo()+" is unloading goods: "+c.getGoodType());
        for (int i=0;i<c.getCanCarry();i+=loadingPerTurn) {
            goodCount += c.get(loadingPerTurn);

            System.out.println("Ship #"+c.getNo()+" now unloaded leftover "+c.getCarries()+" items of "+c.getGoodType()+", Pier becomes: "+goodCount);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
