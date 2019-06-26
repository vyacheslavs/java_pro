package Lesson_6.TradingShips;

public abstract class Stage {
    protected String description;
    public String getDescription() {
        return description;
    }
    public abstract boolean run(Ship c) throws RuntimeException;
}
