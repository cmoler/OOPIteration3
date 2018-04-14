package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.ArmorItem;

public class ArmorEquipStrategy extends InventoryStrategy{

    private ArmorItem armorItem;

    @Override
    public void useStrategy() {

    }


    public ArmorItem getArmorItem() {
        return armorItem;
    }

    public void setArmorItem(ArmorItem armorItem) {
        this.armorItem = armorItem;
    }
}
