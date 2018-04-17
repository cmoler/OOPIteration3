package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ArmorEquipStrategy;

public class ArmorItem extends TakeableItem {

    private ArmorEquipStrategy armorEquipStrategy;
    private int defense;

    public ArmorItem(String name, Command command, int defense) {
        super(name, command);

        armorEquipStrategy = new ArmorEquipStrategy(this);
        this.defense = defense;
    }

    public ArmorItem(String name, ToggleableCommand command) {
        super(name, command);

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

        if (entity.hasItemInInventory(this)) {
            armorEquipStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }
}
