package step4;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private int health = 100;
    private final List<Item> inventory = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void modifyHealth(int amount) {
        health += amount;
        if (health > 100) {
            health = 100;
        }
        if (health < 0) {
            health = 0;
        }
    }

    public List<String> describeInventory() {
        List<String> result = new ArrayList<>();
        for (Item item : inventory) {
            result.addAll(item.getDescriptors(""));
        }
        return result;
    }
}