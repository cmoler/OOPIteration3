package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ArmorEquipStrategy;
import Model.Level.LevelMessenger;

public class ArmorItem extends TakeableItem {

    private ArmorEquipStrategy armorEquipStrategy;
    private int defense;

    protected ArmorItem(String name, ToggleableCommand command, LevelMessenger levelMessenger) {
        super(name, command, levelMessenger);

        armorEquipStrategy = new ArmorEquipStrategy(this);
        this.defense = 0;
    }

    @Override
    public void select() {
        armorEquipStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItem(this)) {
            armorEquipStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }
}
