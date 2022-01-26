package step4;

import java.util.List;

public abstract class Item {
    private final String name;

    public Item(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract List<String> getDescriptors(String indent);
}
