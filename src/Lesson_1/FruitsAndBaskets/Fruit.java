package Lesson_1.FruitsAndBaskets;

public abstract class Fruit {

    protected float weight;
    protected String name;

    Fruit(String name, float weight) {
        this.name = name;
        this.weight = weight;
    }

    public float getWeight() {
        return weight;
    }

}
