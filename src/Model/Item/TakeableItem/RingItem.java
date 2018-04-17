package Model.Item.TakeableItem;

import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.RingEquipStrategy;

public class RingItem extends TakeableItem{

    private RingEquipStrategy ringEquipStrategy;

    public RingItem(String name, ToggleableCommand command) {
        super(name, command);

        ringEquipStrategy = new RingEquipStrategy(this);
    }

    @Override
    public void select() {
        ringEquipStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItemInInventory(this)) {
            ringEquipStrategy.setEntity(entity);
            setDropStrategyEntity(entity);
            setToBeDeleted();
        }
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }
}
