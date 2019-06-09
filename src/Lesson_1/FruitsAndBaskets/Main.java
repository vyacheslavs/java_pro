package Lesson_1.FruitsAndBaskets;

public class Main {
    public static void main(String[] args) {
        Box<Orange> orangeBox = new Box<>();
        orangeBox.addFruit(new Orange());

        Box<Apple> appleBox = new Box<>();
        appleBox.addFruit(new Apple());

        System.out.println("let's compare box of apple and box of orange: " + orangeBox.compareTo(appleBox));

        Box<Orange> otherOrangeBox = new Box<>();
        otherOrangeBox.addFruit(new Orange());
        orangeBox.pourTo(otherOrangeBox);

        Box<Apple> otherAppleBox = new Box<>();
        otherAppleBox.addFruit(new Apple());
        otherAppleBox.addFruit(new Apple());
        appleBox.pourTo(otherAppleBox);

        System.out.println("let's compare other box of apple and box of orange: " + otherOrangeBox.compareTo(otherAppleBox));
    }
}
