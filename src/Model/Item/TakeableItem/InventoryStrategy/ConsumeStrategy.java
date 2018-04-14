package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.ConsumableItem;

public class ConsumeStrategy extends InventoryStrategy{

    private ConsumableItem consumableItem;

    @Override
    public void useStrategy() {

    }

    public void setConsumableItem(ConsumableItem consumableItem) {
        this.consumableItem = consumableItem;
    }

    public ConsumableItem getConsumableItem() {
        return consumableItem;
    }
}
