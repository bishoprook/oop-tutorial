package step4;

import java.util.Collections;
import java.util.List;

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

    @Override
    public List<String> getDescriptors(String indent) {
        return Collections.singletonList(
                String.format("%s%s - %d ATK", indent, getName(), attackPower));
    }
}
