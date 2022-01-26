package step3;

public class Weapon extends Item {
    private final int attackPower;

    public Weapon(String name, int attackPower) {
        super(name);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void attack(Player target) {
        target.modifyHealth(-attackPower);
    }
}
