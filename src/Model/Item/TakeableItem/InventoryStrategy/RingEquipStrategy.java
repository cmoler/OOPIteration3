package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.RingItem;

public class RingEquipStrategy extends InventoryStrategy{

    private RingItem ringItem;

    @Override
    public void useStrategy() {

    }

    public RingItem getRingItem() {
        return ringItem;
    }

    public void setRingItem(RingItem ringItem) {
        this.ringItem = ringItem;
    }
}
