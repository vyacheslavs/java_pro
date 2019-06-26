package Lesson_6.TradingShips;

public class LoadUnloadStage extends Stage {

    private Pier[] availablePiers;
    private boolean loadingStage;

    LoadUnloadStage(boolean loadingStage, Pier ... piers) {
        availablePiers = piers;
        this.loadingStage = loadingStage;
        if (loadingStage)
            this.description = "loading";
        else
            this.description = "unloading";
    }

    @Override
    public boolean run(Ship c) throws RuntimeException {

        // find the pier
        for (Pier p : availablePiers) {
            if (c.getGoodType() == p.getType()) {
                return loadingStage ? p.startLoading(c) : p.startUnloading(c);
            }
        }
        throw new RuntimeException("No corresponding pier available");
    }
}
