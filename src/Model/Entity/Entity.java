package Model.Entity;

import Model.Entity.EntityAttributes.*;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.TakeableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.Terrain;
import Model.Level.Mount;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;

import java.util.ArrayList;
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
    private Mount mount;

    public Entity() {
        this.xpLevel = new XPLevel();
        this.health = new Health(100, 100);
        this.compatableTerrain = new ArrayList<Terrain>();
        compatableTerrain.add(Terrain.GRASS);
        this.velocity = new Vec3d(0,0,0);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation o){
        orientation = o;
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

    public void decrementVelocity(){
        double newX = velocity.x;
        double newY = velocity.y;
        double newZ = velocity.z;

        if (Math.abs(velocity.x) > 0) {
            if (newX > 0) { newX = newX - 1; }
            else { newX = newX + 1; }
        }

        if (Math.abs(velocity.y) > 0) {
            if (newY > 0) { newY = newY - 1; }
            else { newY = newY + 1; }
        }

        if (Math.abs(velocity.z) > 0) {
            if (newZ > 0) { newZ = newZ - 1; }
            else { newZ = newZ + 1; }
        }

        velocity = new Vec3d(newX,newY,newZ);
    }

    public boolean isMounted() { return (mount != null);}

    public void increaseHealth(int amt) {
        health.increaseCurrentHealth(amt);
    }

    public void decreaseHealth(int amt) {
        health.decreaseCurrentHealth(amt);
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

    public Boolean canMoveOnTerrain(Terrain T) {
        return compatableTerrain.contains(T);
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public void notifyObservers(){
        for (LevelViewElement o:observers) {
            o.notifyViewElement();
        }
    }

    public Boolean isMoving(){
        return (velocity.length() >= 1);
    }

    public void mountVehicle(Mount mount){
        this.mount = mount;
        compatableTerrain.addAll(mount.getPassableTerrain());
        mount.setOrientation(getOrientation());
        speed.increaseSpeed(mount.getMovementSpeed());
        // notifyObservers(); Only if we want the sprite to change to a mounted sprite
    }

    public void dismountVehicle(){
        speed.decreaseSpeed(mount.getMovementSpeed());
        compatableTerrain.removeAll(mount.getPassableTerrain());
        this.mount = null;
        // notifyObservers(); Only if we want the sprite to change
    }

    public void equipArmor(ArmorItem armorItem) {
        inventory.removeItem(armorItem);

        if(equipment.hasArmor()) {
            ArmorItem oldArmor = equipment.unequipArmor(this);

            oldArmor.toggleEquipEffect(this);
        }

        equipment.equipArmor(armorItem, this);
    }

    public void equipRing(RingItem ringItem) {
        inventory.removeItem(ringItem);

        if(equipment.hasRing()) {
            RingItem oldRing = equipment.unequipRing(this);

            oldRing.toggleEquipEffect(this);
        }

        equipment.equipRing(ringItem, this);
    }

    public void equipWeapon(WeaponItem weaponItem) {
        equipment.equipWeapon(weaponItem);
    }
}
