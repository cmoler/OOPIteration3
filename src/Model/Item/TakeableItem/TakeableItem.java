package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.NonSettableCommand.DropItemCommand;
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

        if(entity.hasItemInInventory(this)) {
            setDropStrategyEntity(entity);
            setToBeDeleted();
        }
    }

    final public void drop(){
        dropStrategy.useStrategy();
    }

    public void setCurrentLevelMessenger(LevelMessenger levelMessenger) {
        dropStrategy = new DropStrategy(this, new DropItemCommand(levelMessenger));
    }

    protected void setDropStrategyEntity(Entity entity) {
        dropStrategy.setEntity(entity);
    }

    public abstract void select();

    public int getPrice() {
        return price;
    }
}
