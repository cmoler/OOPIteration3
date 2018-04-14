package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ArmorEquipStrategy;

public class ArmorItem extends TakeableItem{

    private ArmorEquipStrategy armorEquipStrategy;
    private int defense;

    protected ArmorItem(String name, Command command) {
        super(name, command);
    }

    @Override
    public void select() {

    }

    @Override
    public void dropItem(Entity entity) {

    }

    @Override
    public void onTouch(Entity entity) {

    }
}
