package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.RingEquipStrategy;

public class RingItem extends TakeableItem{

    private RingEquipStrategy ringEquipStrategy;

    protected RingItem(String name, Command command) {
        super(name, command);
    }

    @Override
    public void select() {

    }

    @Override
    public void onTouch(Entity entity) {

    }
}
