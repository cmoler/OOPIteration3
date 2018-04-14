package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.RingItem;

public class RingEquipStrategy extends InventoryStrategy {

    private RingItem ringItem;

    public RingEquipStrategy(RingItem ringItem) {
        this.ringItem = ringItem;
    }

    @Override
    public void useStrategy() {
        getEntity().equipRing(ringItem);
    }
}
