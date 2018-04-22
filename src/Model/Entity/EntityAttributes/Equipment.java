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

    public void equipWeapon(WeaponItem newWeapon, Entity entity) {
        equippedWeapon = newWeapon;
    }

    public void unequipWeapon(Entity entity) {
        if(hasWeapon()) {
            if (entity.hasFreeSpaceInInventory()) {
                entity.addItemToInventory(equippedWeapon);

                equippedWeapon = null;
            }
        }
    }

    public void equipArmor(ArmorItem newArmor, Entity entity) {
        equippedArmor = newArmor;
        equippedArmor.toggleEquipEffect(entity);
    }

    public void unequipArmor(Entity entity) {
        if(hasArmor()) {
            entity.addItemToInventory(equippedArmor);

            equippedArmor.toggleEquipEffect(entity);

            equippedArmor = null;
        }
    }

    public void equipRing(RingItem equippedRing, Entity entity) {
        this.equippedRing = equippedRing;
        this.equippedRing.toggleEquipEffect(entity);
    }

    public void unequipRing(Entity entity) {
        if (hasRing()) {
            entity.addItemToInventory(equippedRing);

            equippedRing.toggleEquipEffect(entity);

            equippedRing = null;
        }
    }

    public int getRange(){
        if (equippedWeapon != null){
            return equippedWeapon.getRange();
        }
        else{
            return 0;
        }
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

    public void setStrategies(Entity entity) {
        if(equippedWeapon != null) {
            equippedWeapon.setItemStrategyEntity(entity);
            equippedWeapon.setDropStrategyEntity(entity);
        }

        if(equippedArmor != null) {
            equippedArmor.setItemStrategyEntity(entity);
            equippedArmor.setDropStrategyEntity(entity);
        }

        if(equippedRing != null) {
            equippedRing.setItemStrategyEntity(entity);
            equippedRing.setDropStrategyEntity(entity);
        }
    }
}
