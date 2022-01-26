package step4;

import java.util.Collections;
import java.util.List;

public class Potion extends Item {
    private final int healAmount;

    public Potion(String name, int healAmount) {
        super(name);
        this.healAmount = healAmount;
    }

    public int getHealAmount() {
        return healAmount;
    }

    public void quaff(Player target) {
        target.modifyHealth(healAmount);
    }

    @Override
    public List<String> getDescriptors(String indent) {
        return Collections.singletonList(
                String.format("%s%s - heals %d", indent, getName(), healAmount));
    }
}
