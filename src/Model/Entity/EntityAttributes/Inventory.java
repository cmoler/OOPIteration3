package Model.Entity.EntityAttributes;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Item.TakeableItem.TakeableItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Inventory implements Visitable {

    private List<TakeableItem> inventory;
    private int maxSize;

    public Inventory() {
        inventory = new ArrayList<>(10);
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
        inventory.remove(item);
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
        if(0 <= selectedItem && selectedItem <= inventory.size() - 1)
            return inventory.get(selectedItem);
        return null;
    }

    public TakeableItem takeRandomItem() {
        Random random = new Random();

        if(inventory.isEmpty()) {
            return null;
        }

        int randomSlot = random.nextInt(inventory.size());

        TakeableItem item = inventory.get(randomSlot);

        inventory.remove(randomSlot);

        return item;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void accept(Visitor visitor) { // TODO: change? if so how prevent LoD?
        for(TakeableItem item: inventory) {
            item.accept(visitor);
        }
    }
}
