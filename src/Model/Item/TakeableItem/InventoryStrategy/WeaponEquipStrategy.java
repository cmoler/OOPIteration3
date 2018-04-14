package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.WeaponItem;

public class WeaponEquipStrategy extends InventoryStrategy{

    private WeaponItem weaponItem;

    @Override
    public void useStrategy() {

    }

    public void setWeaponItem(WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
    }

    public WeaponItem getWeaponItem() {
        return weaponItem;
    }
}
