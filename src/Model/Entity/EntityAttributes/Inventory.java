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
        inventory.add(item);
    }

    public void removeItem(TakeableItem item) {
        if(inventory.contains(item)) {
            inventory.remove(item);
        }
    }

    public void raiseItemLimit(int increase) {
        maxSize += increase;
    }
}
