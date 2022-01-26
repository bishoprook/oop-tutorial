package step1;

public class Weapon {
    private final String name;
    private final int attackPower;

    public Weapon(String name, int attackPower) {
        this.name = name;
        this.attackPower = attackPower;
    }

    public String getName() {
        return name;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Player target) {
        target.modifyHealth(-attackPower);
    }
}
