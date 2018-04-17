package Model.Item.TakeableItem.InventoryStrategy;

import Model.Command.EntityCommand.NonSettableCommand.DropItemCommand;
import Model.Item.TakeableItem.TakeableItem;

public class DropStrategy extends InventoryStrategy{

    private TakeableItem item;
    private DropItemCommand dropItemCommand;

    public DropStrategy(TakeableItem item, DropItemCommand dropItemCommand) {
        this.item = item;
        this.dropItemCommand = dropItemCommand;
    }

    @Override
    public void useStrategy() {
        dropItemCommand.setItem(item);
        dropItemCommand.execute(getEntity());
    }
}
