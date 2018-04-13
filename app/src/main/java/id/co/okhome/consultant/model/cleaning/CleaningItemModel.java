package id.co.okhome.consultant.model.cleaning;

import java.io.Serializable;
import java.util.Random;

public class CleaningItemModel implements Serializable {
    public int id;
    public String name;
    public int price;
    public float duration;

    public CleaningItemModel(String name, int price, float duration) {
        id = Math.abs(new Random().nextInt());
        this.name = name;
        this.price = price;
        this.duration = duration;
    }

    public CleaningItemModel() {
    }
}