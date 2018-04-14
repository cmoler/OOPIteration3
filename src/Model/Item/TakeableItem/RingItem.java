package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.RingEquipStrategy;
import Model.Level.LevelMessenger;

public class RingItem extends TakeableItem{

    private RingEquipStrategy ringEquipStrategy;

    protected RingItem(String name, ToggleableCommand command, LevelMessenger levelMessenger) {
        super(name, command, levelMessenger);

        ringEquipStrategy = new RingEquipStrategy(this);
    }

    @Override
    public void select() {
        ringEquipStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItem(this)) {
            ringEquipStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }
}
