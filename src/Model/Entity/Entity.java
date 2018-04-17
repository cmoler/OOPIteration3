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
import java.util.Random;

public class Entity {

    private List<LevelViewElement> observers;

    private ItemHotBar hotBar;

    private List<Skill> weaponSkills;
    private List<Skill> nonWeaponSkills;
    private int currentlySelectedSkill = 0;

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
    private boolean moveable;
    private Mount mount;

    public Entity() {
        this.xpLevel = new XPLevel();
        this.health = new Health(100, 100);
        this.inventory = new Inventory();
        this.equipment = new Equipment();
        this.compatableTerrain = new ArrayList<Terrain>();
        compatableTerrain.add(Terrain.GRASS);
        this.velocity = new Vec3d(0,0,0);
        skillLevelsMap = new HashMap<>();
        weaponSkills = new ArrayList<>();
        nonWeaponSkills = new ArrayList<>();
        hotBar = new ItemHotBar(this);
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
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

    public boolean hasItemInInventory(TakeableItem item) {
        return inventory.hasItem(item);
    }

    public int getCurrentHealth() {
        return health.getCurrentHealth();
    }

    public int getMaxHealth() {
        return health.getMaxHealth();
    }

    public int getNoise() { return noiseLevel.getNoise(); }

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

    public Boolean canMoveOnTerrain(Terrain T) {
        return compatableTerrain.contains(T);
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void addVelocity(Vec3d add){
        velocity.add(add);
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

    public void equipWeapon(WeaponItem weaponItem) {
        inventory.removeItem(weaponItem);

        equipment.unequipWeapon(this);

        equipment.equipWeapon(weaponItem, this);
    }

    public void unequipWeapon() {
        if(inventory.hasFreeSpace()) {
            equipment.unequipWeapon(this);
        }
    }

    public void equipArmor(ArmorItem armorItem) {
        inventory.removeItem(armorItem);

        equipment.unequipArmor(this);

        equipment.equipArmor(armorItem, this);
    }

    public void unequipArmor() {
        if(inventory.hasFreeSpace()) {
            equipment.unequipArmor(this);
        }
    }

    public void equipRing(RingItem ringItem) {
        inventory.removeItem(ringItem);

        equipment.unequipRing(this);

        equipment.equipRing(ringItem, this);
    }

    public void unequipRing() {
        if(inventory.hasFreeSpace()) {
            equipment.unequipRing(this);
        }
    }

    public HashMap<Skill, SkillLevel> getSkillLevelsMap() {
        return skillLevelsMap;
    }

    public boolean hasSkill(Skill hostSkill) {
        return skillLevelsMap.containsKey(hostSkill);
    }

    public void addSkillsToMap(Skill... skill) {
        for(Skill addingSkill: skill) {
            if (!skillLevelsMap.containsKey(addingSkill)) {
                skillLevelsMap.put(addingSkill, new SkillLevel(1));
            }
        }
    }

    public void addWeaponSkills(Skill... weaponSkills) {
        for(Skill weaponSkill: weaponSkills) {
            if(!this.weaponSkills.contains(weaponSkill)) {
                this.weaponSkills.add(weaponSkill);
                addSkillsToMap(weaponSkills);
            }
        }
    }

    public void addNonWeaponSkills(Skill... nonWeaponSkills) {
        for(Skill nonWeaponSkill: nonWeaponSkills) {
            if(!this.nonWeaponSkills.contains(nonWeaponSkill)) {
                this.nonWeaponSkills.add(nonWeaponSkill);
                addSkillsToMap(nonWeaponSkills);
            }
        }
    }

    public WeaponItem getWeaponItem() {
        return equipment.getEquippedWeapon();
    }
    
    public void attack() {
        getWeaponItem().attack(this);
    }

    public void addItemToHotBar(TakeableItem takeableItem, int index){
        hotBar.addItem(takeableItem, index);
    }

    public void useHotBar(int index){
        hotBar.use(index);
    }

    public void useSkill(int index){
        if(nonWeaponSkills.size() - 1 < index || index < 0) return;
        else{
            nonWeaponSkills.get(index).fire(this);
        }
    }

    public void useSkill(){
        nonWeaponSkills.get(currentlySelectedSkill).fire(this);
    }

    public void scrollLeft(){
        if(currentlySelectedSkill <= 0) currentlySelectedSkill = nonWeaponSkills.size() - 1;
        else currentlySelectedSkill --;
    }

    public void scrollRight(){
        if(currentlySelectedSkill >= nonWeaponSkills.size() - 1) currentlySelectedSkill = 0;
        else currentlySelectedSkill ++;
    }

    public SkillLevel getSkillLevel(Skill skill) {
        if (skillLevelsMap.containsKey(skill)) {
            return skillLevelsMap.get(skill);
        }

        else
            return null;
    }

    public boolean hasFreeSpaceInInventory() {
        return inventory.hasFreeSpace();
    }

    public void setNoiseLevel(NoiseLevel noiseLevel) {
        this.noiseLevel = noiseLevel;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setSkillLevels(HashMap<Skill, SkillLevel> skillLevels) {
        this.skillLevelsMap = skillLevels;
    }

    public void addGold(int price) {
        gold.increaseGold(price);
    }

    public void removeGold(int price) {
        gold.decreaseGold(price);
    }

    public int getGold() {
        return gold.getGoldAmount();
    }

    public TakeableItem takeRandomItemFromInventory() {
        return inventory.takeRandomItem();
    }

    public String getRandomFacts(int observeStrength) { // TODO: make more complex random-ness for observe
        Random random = new Random();

        int randomAttribute = random.nextInt(5);

        int error = 100 - observeStrength;

        if(error < 0) {
            error = 0;
        }

        switch (randomAttribute) {
            case 0:
                int currHPGuess = getCurrentHealth() + (error / 10 * (random.nextInt(3)) - 1);
                return "Current HP: " + currHPGuess;
            case 1:
                int manaGuess = getCurrentMana() + (error / 10 * (random.nextInt(3)) - 1);
                return "Current Mana: " + manaGuess;
            case 2:
                int goldGuess = getCurrentGold() + (error / 10 * (random.nextInt(3)) - 1);
                return "Current Gold: " + goldGuess;
            case 3:
                int maxHPGuess = getMaxHealth() + (error / 10 * (random.nextInt(3)) - 1);
                return "Max HP: " + maxHPGuess;
            default: return "Nothing to report!";
        }
    }

    public int getCurrentMana() {
        return mana.getCurrentMana();
    }

    public int getCurrentGold() {
        return gold.getGold();
    }

    public SightRadius getSightRadius() {
        return sightRadius;
    }

    public void setSightRadius(SightRadius sightRadius) {
        this.sightRadius = sightRadius;
    }

    public int getSight(){
        return sightRadius.getSight();
    }
}
