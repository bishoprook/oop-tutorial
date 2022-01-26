package step2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag {
    private final String name;
    private final int capacity;
    private int count = 0;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();
    private final List<Bag> bags = new ArrayList<>();

    public Bag(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCount() {
        return count;
    }

    /*
     * These getters return a read-only copy of the list to enforce that you
     * must use the "add" methods below. Ensures nobody can push us past our
     * capacity.
     */
    public List<Weapon> getWeapons() {
        return Collections.unmodifiableList(weapons);
    }

    public List<Potion> getPotions() {
        return Collections.unmodifiableList(potions);
    }

    public List<Bag> getBags() {
        return Collections.unmodifiableList(bags);
    }

    public void incrementCount() {
        if (count == capacity) {
            throw new RuntimeException("Already at capacity");
        }
        count++;
    }

    public void addWeapon(Weapon weapon) {
        incrementCount();
        weapons.add(weapon);
    }

    public void addPotion(Potion potion) {
        incrementCount();
        potions.add(potion);
    }

    public void addBag(Bag bag) {
        incrementCount();
        bags.add(bag);
    }
}
