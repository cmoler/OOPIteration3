package Model.Entity.EntityAttributes;

import Model.Item.TakeableItem.TakeableItem;

import java.util.ArrayList;
import java.util.List;

public class Inventory {

    private List<TakeableItem> inventory;
    private int maxSize;

    public Inventory() {
        inventory = new ArrayList<>();
        maxSize = 10;
    }

    public Inventory(List<TakeableItem> inventory, int maxSize) {
        this.inventory = inventory;
        this.maxSize = maxSize;
    }

    public void addItem(TakeableItem item) {
        if(inventory.size() + 1 <= maxSize) {
            inventory.add(item);
        }
    }

    public void removeItem(TakeableItem item) {
        if(inventory.contains(item)) {
            inventory.remove(item);
        }
    }

    public boolean hasItem(TakeableItem item) {
        return inventory.contains(item);
    }

    public boolean hasFreeSpace() {
        return inventory.size() != maxSize;
    }

    public void raiseItemLimit(int increase) {
        maxSize += increase;
    }

    public int size() {
        return inventory.size();
    }

    public TakeableItem getItem(int selectedItem) {
        if(inventory.size() < selectedItem - 1 && selectedItem > 0)
            return inventory.get(selectedItem);
        return null;
    }
}
