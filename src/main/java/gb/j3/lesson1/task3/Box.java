package gb.j3.lesson1.task3;

import java.util.ArrayList;

public class Box <T extends Fruit> {

    private ArrayList<T> box = new ArrayList<>();

    public final float getWeight() {
        float result = 0.0f;
        for (Fruit fr : box) {
            result += fr.getWeight(); // тут сложение всех фруктов в ящике, умножения не было :(
        }
        return result;
    }

    public final boolean compare(Box<? extends Fruit> anotherBox) {
        return (this.getWeight() - anotherBox.getWeight()) < 0.0001;
    }

    public final void transferFruits(Box <T> anotherBox) {
        anotherBox.box.addAll(box);
        box.clear();
    }

    public final void addFruit(T fruit, int quantity){
        for(int i = 0; i < quantity; i++){
            box.add(fruit);
        }
    }
}
