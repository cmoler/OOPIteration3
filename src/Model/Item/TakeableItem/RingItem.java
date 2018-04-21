package Model.Item.TakeableItem;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.RingEquipStrategy;

public class RingItem extends TakeableItem {

    private RingEquipStrategy ringEquipStrategy;

    public RingItem(String name, ToggleableCommand command) {
        super(name, command);

        ringEquipStrategy = new RingEquipStrategy(this);
    }

    @Override
    public void select() {
        ringEquipStrategy.useStrategy();
    }

    protected void setItemStrategyEntity(Entity entity) {
        ringEquipStrategy.setEntity(entity);
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }

    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }
}
