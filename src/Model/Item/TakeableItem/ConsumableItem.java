package Model.Item.TakeableItem;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ConsumeStrategy;

public class ConsumableItem extends TakeableItem {

    private ConsumeStrategy consumeStrategy;

    public ConsumableItem(String name, Command command) {
        super(name, command);

        this.consumeStrategy = new ConsumeStrategy(this);
    }

    @Override
    public void select() {
        consumeStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItemInInventory(this)) {
            consumeStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    public void consume(Entity entity) {
        executeCommand(entity);
        entity.removeItemFromInventory(this);
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitItem(this);
    }
}
