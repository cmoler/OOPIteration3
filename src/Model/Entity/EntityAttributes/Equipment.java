package Model.Entity.EntityAttributes;

import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;

public class Equipment {

    private WeaponItem equippedWeapon;
    private ArmorItem equippedArmor;
    private RingItem equippedRing;

    public Equipment() {
        this.equippedWeapon = null;
        this.equippedArmor = null;
        this.equippedRing = null;
    }

    public Equipment(WeaponItem equippedWeapon, ArmorItem equippedArmor, RingItem equippedRing) {
        this.equippedWeapon = equippedWeapon;
        this.equippedArmor = equippedArmor;
        this.equippedRing = equippedRing;
    }

    public boolean hasWeapon() {
        return equippedWeapon != null;
    }

    public boolean hasArmor() {
        return equippedArmor != null;
    }

    public boolean hasRing() {
        return equippedRing != null;
    }

    public void equipWeapon(WeaponItem equippedWeapon) {
        this.equippedWeapon = equippedWeapon;
    }

    public void equipArmor(ArmorItem equippedArmor) {
        this.equippedArmor = equippedArmor;
    }

    public void equipRing(RingItem equippedRing) {
        this.equippedRing = equippedRing;
    }
}
