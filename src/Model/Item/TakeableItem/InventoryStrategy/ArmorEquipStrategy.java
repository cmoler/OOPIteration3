package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.ArmorItem;

public class ArmorEquipStrategy extends InventoryStrategy{

    private ArmorItem armorItem;

    public ArmorEquipStrategy(ArmorItem armorItem) {
        this.armorItem = armorItem;
    }

    @Override
    public void useStrategy() {
        getEntity().equipArmor(armorItem);
    }
}
