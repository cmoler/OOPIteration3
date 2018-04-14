package Model.Item.TakeableItem.InventoryStrategy;

import Model.Command.LevelCommand.DropItemCommand;
import Model.Item.TakeableItem.TakeableItem;

public class DropStrategy extends InventoryStrategy{

    private TakeableItem item;

    private DropItemCommand dropItemCommand;

    @Override
    public void useStrategy() {

    }

    public void setItem(TakeableItem takeableItem){
        this.item = takeableItem;
    }

    public TakeableItem getItem() {
        return item;
    }
}
