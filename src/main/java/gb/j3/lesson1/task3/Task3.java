package gb.j3.lesson1.task3;

public class Task3 {

    public static void main(String[] args) {

        Box<Orange> orangeBox = new Box<>();
        Box<Orange> orangeBox1 = new Box<>();
        Box<Apple> appleBox = new Box<>();
        Box<Apple> appleBox1 = new Box<>();

        System.out.println("Task3");

        System.out.println("g:");
        orangeBox.addFruit(new Orange(),10);
        orangeBox1.addFruit(new Orange(),12);
        appleBox.addFruit(new Apple(),15);
        appleBox1.addFruit(new Apple(),4);

        System.out.println("Box 1: " + orangeBox.getWeight());
        System.out.println("Box 2: " + orangeBox1.getWeight());
        System.out.println("Box 3: " + appleBox.getWeight());
        System.out.println("Box 4: " + appleBox1.getWeight());

        System.out.println("e:");
        System.out.println("Равенство Box 1 и Box 3: " + orangeBox.compare(appleBox));
        System.out.println("Равенство Box 2 и Box 4: " + orangeBox1.compare(appleBox1));
        System.out.println("Равенство Box 3 и Box 4: " + appleBox.compare(appleBox1));

        System.out.println("f и d:");
        orangeBox.transferFruits(orangeBox1);
        appleBox.transferFruits(appleBox1);
        System.out.println("Box 1: " + orangeBox.getWeight());
        System.out.println("Box 2: " + orangeBox1.getWeight());
        System.out.println("Box 3: " + appleBox.getWeight());
        System.out.println("Box 4: " + appleBox1.getWeight());
    }
}
