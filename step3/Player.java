package step3;

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
        return describeInventoryInternal("", inventory);
    }

    private static List<String> describeInventoryInternal(String indent, List<Item> items) {
        List<String> result = new ArrayList<>();
        for (Item item : items) {
            if (item instanceof Weapon) {
                Weapon weapon = (Weapon) item;
                result.add(String.format(
                        "%s%s - %d ATK", indent, weapon.getName(), weapon.getAttackPower()));
            } else if (item instanceof Potion) {
                Potion potion = (Potion) item;
                result.add(String.format(
                        "%s%s - heals %d", indent, potion.getName(), potion.getHealAmount()));
            } else if (item instanceof Bag) {
                Bag bag = (Bag) item;
                result.add(String.format(
                        "%s%s - (%d/%d)", indent, bag.getName(), bag.getCount(), bag.getCapacity()));
                result.addAll(
                        describeInventoryInternal(indent + "  ", bag.getContents()));
            }
        }
        return result;
    }
}