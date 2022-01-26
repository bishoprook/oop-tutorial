package step2;

public class Potion {
    private final String name;
    private final int healAmount;

    public Potion(String name, int healAmount) {
        this.name = name;
        this.healAmount = healAmount;
    }

    public String getName() {
        return name;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void quaff(Player target) {
        target.modifyHealth(healAmount);
    }
}
