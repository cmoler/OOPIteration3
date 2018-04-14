package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.LevelCommand.DropItemCommand;
import Model.Entity.Entity;
import Model.Item.Item;
import Model.Item.TakeableItem.InventoryStrategy.DropStrategy;
import Model.Level.LevelMessenger;

public abstract class TakeableItem extends Item {

    private int price; // TODO: constructors/getters/setters for price of item
    private DropStrategy dropStrategy;

    protected TakeableItem(String name, Command command) {
        super(name, command);
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if(entity.hasItem(this)) {
            dropStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    final public void dropItem(){
        dropStrategy.useStrategy();
    }

    public void setCurrentLevelMessenger(LevelMessenger levelMessenger) {
        dropStrategy = new DropStrategy(this, new DropItemCommand(levelMessenger));
    }

    public abstract void select();
}
