package Lesson_6.TradingShips;

import java.util.concurrent.Semaphore;

public class TransferStage extends Stage {

    boolean isDirect;
    Semaphore obsticle = new Semaphore(2);

    TransferStage(boolean isDirect) {
        this.isDirect = isDirect;
        if (isDirect)
            description = "transferToDestination";
        else
            description = "transferToSource";
    }

    @Override
    public boolean run(Ship c) throws RuntimeException {

        try {
            obsticle.acquire();

            for (int i = 0; i < 5; i++) {
                if (isDirect)
                    System.out.println("Ship #"+c.getNo()+" is (still) transfering goods, typeof: "+c.getGoodType());
                else
                    System.out.println("Ship #"+c.getNo()+" is coming back");
                Thread.sleep(1000);
            }
            obsticle.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;

    }
}
