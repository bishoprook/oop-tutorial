package step4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Bag extends Item {
    private final int capacity;
    private int count = 0;
    private final List<Item> contents = new ArrayList<>();

    public Bag(String name, int capacity) {
        super(name);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCount() {
        return count;
    }

    /*
     * This getter returns a read-only copy of the list to enforce that you
     * must use the "add" method below. Ensures nobody can push us past our
     * capacity.
     */
    public List<Item> getContents() {
        return Collections.unmodifiableList(contents);
    }

    public void addItem(Item item) {
        if (count == capacity) {
            throw new RuntimeException("Already at capacity");
        }
        count++;
        contents.add(item);
    }

    @Override
    public List<String> getDescriptors(String indent) {
        List<String> result = new ArrayList<>();
        result.add(String.format("%s%s - (%d/%d)", indent, getName(), count, capacity));
        for (Item item : contents) {
            result.addAll(item.getDescriptors(indent + "  "));
        }
        return result;
    }
}
