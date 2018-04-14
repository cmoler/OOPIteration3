package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ConsumeStrategy;

public class ConsumableItem extends TakeableItem{

    private ConsumeStrategy consumeStrategy;

    protected ConsumableItem(String name, Command command) {
        super(name, command);
    }

    @Override
    public void select() {

    }

    @Override
    public void onTouch(Entity entity) {

    }
}
