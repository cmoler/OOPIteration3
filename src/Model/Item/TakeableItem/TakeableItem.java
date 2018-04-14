package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Item.Item;
import Model.Item.TakeableItem.InventoryStrategy.DropStrategy;

public abstract class TakeableItem extends Item{

    private int price;
    private DropStrategy dropStrategy;

    protected TakeableItem(String name, Command command) {
        super(name, command);
    }

    public abstract void select();

    public void dropItem(Entity entity){
        dropStrategy.useStrategy();
    }
}
