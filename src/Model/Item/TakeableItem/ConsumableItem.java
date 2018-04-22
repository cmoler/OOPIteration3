package Model.Item.TakeableItem;

import Controller.Visitor.Visitor;
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
    public boolean usableByEntity(Entity entity) {
        return true;
    }

    public void setItemStrategyEntity(Entity entity) {
        consumeStrategy.setEntity(entity);
    }

    public void consume(Entity entity) {
        executeCommand(entity);
        entity.removeItemFromInventory(this);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }
}
