package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.ConsumableItem;

public class ConsumeStrategy extends InventoryStrategy{

    private ConsumableItem consumableItem;

    public ConsumeStrategy(ConsumableItem consumableItem) {
        this.consumableItem = consumableItem;
    }

    @Override
    public void useStrategy() {
        consumableItem.consume(getEntity());
    }
}
