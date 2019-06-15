package Lesson_1.FruitsAndBaskets;

import java.util.ArrayList;

public final class Box<FruitType extends Fruit> {
    private ArrayList<FruitType> fruits = new ArrayList<>();

    public float getWeight() {
        float retVal = 0.0f;
        for (FruitType fruit : fruits) {
            retVal += fruit.getWeight();
        }
        return retVal;
    }

    public void addFruit(FruitType f) {
        fruits.add(f);
    }

    public boolean compareTo(Box<?> other) {
        return this.getWeight() == other.getWeight();
    }

    public void pourTo(Box<FruitType> other) {
        for (FruitType f : fruits)
            other.addFruit(f);
        fruits.clear();
    }
}
