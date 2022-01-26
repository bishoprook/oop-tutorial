package step1;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private int health = 100;
    private final List<Weapon> weapons = new ArrayList<>();
    private final List<Potion> potions = new ArrayList<>();

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public void addWeapon(Weapon weapon) {
        weapons.add(weapon);
    }

    public void addPotion(Potion potion) {
        potions.add(potion);
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
        for (Weapon weapon : weapons) {
            result.add(String.format(
                    "%s - %d ATK", weapon.getName(), weapon.getAttackPower()));
        }
        for (Potion potion : potions) {
            result.add(String.format(
                    "%s - heals %d", potion.getName(), potion.getHealAmount()));
        }
        return result;
    }
}