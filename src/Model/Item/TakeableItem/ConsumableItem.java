package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ConsumeStrategy;
import Model.Level.LevelMessenger;

public class ConsumableItem extends TakeableItem {

    private ConsumeStrategy consumeStrategy;

    protected ConsumableItem(String name, Command command, LevelMessenger levelMessenger) {
        super(name, command, levelMessenger);

        this.consumeStrategy = new ConsumeStrategy(this);
    }

    @Override
    public void select() {
        consumeStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItem(this)) {
            consumeStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    public void consume(Entity entity) {
        executeCommand(entity);
        entity.removeItemFromInventory(this);
    }
}
