package Model.Item.TakeableItem.InventoryStrategy;

import Model.Item.TakeableItem.WeaponItem;

public class WeaponEquipStrategy extends InventoryStrategy {

    private WeaponItem weaponItem;

    public WeaponEquipStrategy(WeaponItem weaponItem) {
        this.weaponItem = weaponItem;
    }

    @Override
    public void useStrategy() {
        if(getEntity().hasSkill(weaponItem.getHostSKill())) {
            getEntity().equipWeapon(weaponItem);
        }
    }
}
