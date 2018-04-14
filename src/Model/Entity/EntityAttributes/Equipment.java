package Model.Entity.EntityAttributes;

import Model.Entity.Entity;
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

    public void unequipWeapon(Entity entity) {
        this.equippedArmor = equippedArmor;
        this.equippedArmor.toggleEquipEffect(entity);
    }

    public void equipArmor(ArmorItem equippedArmor, Entity entity) {
        this.equippedArmor = equippedArmor;
        this.equippedArmor.toggleEquipEffect(entity);
    }

    public ArmorItem unequipArmor(Entity entity) {
        equippedArmor.toggleEquipEffect(entity);

        ArmorItem oldArmor = equippedArmor;
        equippedArmor = null;

        return oldArmor;
    }

    public void equipRing(RingItem equippedRing, Entity entity) {
        this.equippedRing = equippedRing;
        this.equippedRing.toggleEquipEffect(entity);
    }

    public RingItem unequipRing(Entity entity) {
        equippedRing.toggleEquipEffect(entity);

        RingItem oldRing = equippedRing;
        equippedRing = null;

        return oldRing;
    }

    public WeaponItem getEquippedWeapon() {
        return equippedWeapon;
    }

    public ArmorItem getEquippedArmor() {
        return equippedArmor;
    }

    public RingItem getEquippedRing() {
        return equippedRing;
    }
}
