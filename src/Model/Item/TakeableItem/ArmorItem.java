package Model.Item.TakeableItem;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.Command;
import Model.Entity.Entity;
import Model.Item.TakeableItem.InventoryStrategy.ArmorEquipStrategy;

public class ArmorItem extends TakeableItem {

    private ArmorEquipStrategy armorEquipStrategy;
    private int defense;

    public ArmorItem(String name, ToggleableCommand command, int defense) {
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

    public void setItemStrategyEntity(Entity entity) {
        armorEquipStrategy.setEntity(entity);
    }

    public void toggleEquipEffect(Entity entity) {
        executeCommand(entity);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }

    public int getDefense() {
        return defense;
    }
}
