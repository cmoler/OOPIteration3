package Model.Entity;

import Model.Entity.EntityAttributes.*;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.TakeableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.Terrain;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import java.util.HashMap;
import java.util.List;

public class Entity {

    private List<LevelViewElement> observers;
    private List<Skill> weaponSkills;
    private List<Skill> nonWeaponSkills;
    private HashMap<Skill, SkillLevel> skillLevelsMap;
    private Vec3d velocity;
    private NoiseLevel noiseLevel;
    private SightRadius sightRadius;
    private XPLevel xpLevel;
    private Health health;
    private Mana mana;
    private Speed speed;
    private Gold gold;
    private Attack attack;
    private Defense defense;
    private Equipment equipment;
    private Inventory inventory;
    private Orientation orientation;
    private List<Terrain> compatableTerrain;
    private boolean sneaking;
    private boolean moveable;

    public Entity() {
        this.xpLevel = new XPLevel();
        this.health = new Health(100, 100);
        this.inventory = new Inventory();
        this.equipment = new Equipment();
    }

    public void addItemToInventory(TakeableItem item) {
        inventory.addItem(item);
    }

    public void removeItemFromInventory(TakeableItem item) {
        inventory.removeItem(item);
    }

    public boolean hasItem(TakeableItem item) {
        return inventory.hasItem(item);
    }

    public int getCurrentHealth() {
        return health.getCurrentHealth();
    }

    public int getMaxHealth() {
        return health.getMaxHealth();
    }

    public boolean isDead() {
        return health.getCurrentHealth() == 0;
    }

    public void increaseHealth(int amt) {
        health.increaseCurrentHealth(amt);
    }

    public void increaseMaxHealth(int amt) {
        health.increaseMaxHealth(amt);
    }

    public void decreaseHealth(int amt) {
        health.decreaseCurrentHealth(amt);
    }

    public void decreaseMaxHealth(int amt) {
        health.decreaseMaxHealth(amt);
    }

    public void increaseMana(int amt){
        mana.increaseMana(amt);
    }

    public void decreaseMana(int amt){
        mana.increaseMana(amt);
    }

    public void increaseNoiseLevel(int amt) {
        noiseLevel.increaseNoise(amt);
    }

    public void decreaseNoiseLevel(int amt) {
        noiseLevel.decreaseNoise(amt);
    }

    public void increaseSpeed(int amt) {
        speed.increaseSpeed(amt);
    }

    public void decreaseSpeed(int amt){
        speed.decreaseSpeed(amt);
    }

    public void levelUp() {
        xpLevel.increaseLevel();
    }

    public int getLevel() {
        return xpLevel.getLevel();
    }

    public void kill() {
        health.decreaseCurrentHealth(health.getMaxHealth());
    }

    public void equipArmor(ArmorItem armorItem) {
        inventory.removeItem(armorItem);

        if(equipment.hasArmor()) {
            ArmorItem oldArmor = equipment.unequipArmor(this);
            // TODO: fix LoD violations here
            oldArmor.toggleEquipEffect(this);
        }

        equipment.equipArmor(armorItem, this);
    }

    public void unequipArmor() {
        if(inventory.hasFreeSpace()) {
            ArmorItem armor = equipment.unequipArmor(this);
            inventory.addItem(armor);
        }
    }

    public void equipRing(RingItem ringItem) {
        inventory.removeItem(ringItem);

        if(equipment.hasRing()) {
            RingItem oldRing = equipment.unequipRing(this);
            // TODO: fix LoD violations here
            oldRing.toggleEquipEffect(this);
        }

        equipment.equipRing(ringItem, this);
    }

    public void unequipRing() {
        if(inventory.hasFreeSpace()) {
            RingItem ring = equipment.unequipRing(this);
            inventory.addItem(ring);
        }
    }

    public void equipWeapon(WeaponItem weaponItem) {
        equipment.equipWeapon(weaponItem);
    }

    public void unequipWeapon() {
        if(inventory.hasFreeSpace()) {            // TODO: fix LoD violations here
           // WeaponItem weaponItem = equipment.unequipWeapon(this);
          //  inventory.addItem(ring);
        }
    }
}