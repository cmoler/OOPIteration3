package Model.Entity;


import Controller.Visitor.SavingVisitor;
import Model.Entity.EntityAttributes.*;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.TakeableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.Mount;
import Model.Level.Terrain;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Entity {

    private LevelViewElement observer;

    private HashMap<Skill, SkillLevel> skillLevelsMap;
    private int currentlySelectedSkill;
    private int currentlySelectedItem;
    private List<Skill> weaponSkills;
    private List<Skill> nonWeaponSkills;

    private Orientation orientation;
    private Vec3d velocity;

    private SightRadius sightRadius;
    private NoiseLevel noiseLevel;

    private XPLevel xpLevel;
    private Health health;
    private Mana mana;
    private Gold gold;
    private Speed speed;
    private Attack attack;
    private Defense defense;

    private Equipment equipment;
    private Inventory inventory;
    private int traversalStrength;

    private ItemHotBar hotBar;

    private List<Terrain> compatableTerrain;
    private boolean moveable;

    private Mount mount;
    private List<Entity> targetingList;
    private List<Entity> friendlyList;

    private String name;

    private long nextMoveTime = 0;

    public Entity(LevelViewElement observer, ItemHotBar hotBar, List<Skill> weaponSkills,
                  List<Skill> nonWeaponSkills, HashMap<Skill, SkillLevel> skillLevelsMap,
                  Vec3d velocity, NoiseLevel noiseLevel, SightRadius sightRadius, XPLevel xpLevel, Health health,
                  Mana mana, Speed speed, Gold gold, Attack attack, Defense defense, Equipment equipment,
                  Inventory inventory, Orientation orientation, List<Terrain> compatableTerrain, boolean moveable,
                  Mount mount, ArrayList<Entity> targetingList, ArrayList<Entity> friendlylist) {
        this.observer = observer;
        this.hotBar = hotBar;
        this.weaponSkills = weaponSkills;
        this.nonWeaponSkills = nonWeaponSkills;
        this.skillLevelsMap = skillLevelsMap;
        this.velocity = velocity;
        this.noiseLevel = noiseLevel;
        this.sightRadius = sightRadius;
        this.xpLevel = xpLevel;
        this.health = health;
        this.mana = mana;
        this.speed = speed;
        this.gold = gold;
        this.attack = attack;
        this.defense = defense;
        this.equipment = equipment;
        this.inventory = inventory;
        this.orientation = orientation;
        this.compatableTerrain = compatableTerrain;
        this.moveable = moveable;
        this.mount = mount;
        this.targetingList = targetingList;
        this.friendlyList = friendlylist;
        traversalStrength = 1;
    }

    public Entity() {
        skillLevelsMap = new HashMap<>();
        currentlySelectedSkill = 0;
        currentlySelectedItem = 0;
        weaponSkills = new ArrayList<>();
        nonWeaponSkills = new ArrayList<>();
        observer = null;

        orientation = Orientation.NORTH;
        velocity = new Vec3d(0,0,0);

        sightRadius = new SightRadius();
        sightRadius.setSight(2);
        noiseLevel = new NoiseLevel();

        xpLevel = new XPLevel();
        health = new Health(100, 100);
        mana = new Mana();
        gold = new Gold();
        speed = new Speed();
        attack = new Attack();
        defense = new Defense();

        inventory = new Inventory();
        equipment = new Equipment();
        hotBar = new ItemHotBar();
        friendlyList = new ArrayList<>();
        orientation = Orientation.NORTH;

        compatableTerrain = new ArrayList<>();
        compatableTerrain.add(Terrain.GRASS);
        moveable = true;

        mount = null;
        targetingList = new ArrayList<>();
        traversalStrength = 1;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public boolean canMove() {
        return System.nanoTime() > nextMoveTime;
    }

    public void setNextMoveTime() {
        nextMoveTime = System.nanoTime() + speed.getSpeed();
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation o){
        orientation = o;
        notifyObservers(null);
    }

    public int getRange(){
        return equipment.getRange();
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
        if(health.getCurrentHealth() <= 0) {
            observer.notifyViewElementDeath();
        }
    }

    public void decreaseMaxHealth(int amt) {
        health.decreaseMaxHealth(amt);
    }

    public void increaseMana(int amt){
        mana.increaseMana(amt);
    }

    public void decreaseMana(int amt){
        mana.decreaseMana(amt);
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

    public List<Entity> getTargetingList() {
        return targetingList;
    }

    public void setTargetingList(List<Entity> targetingList) {
        this.targetingList = targetingList;
    }

    public boolean targets(Entity entity){
        return targetingList.contains(entity);
    }

    public void addTarget(Entity ent){
        targetingList.add(ent);
    }

    public void removeTarget(Entity ent){
        targetingList.remove(ent);
    }

    public List<Entity> getFriendlyList(){
        return friendlyList;
    }

    public void setFriendlyList(List<Entity> friendlyList){
        this.friendlyList = friendlyList;
    }

    public void addFriendly(Entity entity){
        friendlyList.add(entity);
    }

    public void removeFriendly(Entity entity){
        friendlyList.remove(entity);
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
        if(mount != null) return mount.getPassableTerrain().contains(T);
        else return compatableTerrain.contains(T);
    }

    public Vec3d getVelocity() {
        return velocity;
    }

    public void addVelocity(Vec3d add){
        if(isMoveable()) velocity.add(add);
    }

    public void addVelocityFromControllerInput(Vec3d add){
        if(canMove()) {
            addVelocity(add);
            setNextMoveTime();
        }
    }

    public void setVelocity(Vec3d velocity) {
        this.velocity = velocity;
    }

    public void notifyUponDeath(){
        observer.notifyViewElementDeath();
    }

    public void notifyObservers(Point3D position) {
        if (observer != null) {
            observer.notifyViewElement();
            if (position != null) {
                observer.setPosition(position);
            }
        }
    }

    public LevelViewElement getObserver() {
        return observer;
    }

    public void setObserver(LevelViewElement observer) {
        this.observer = observer;
    }

    public Boolean isMoving(){
        return (velocity.length() >= 1);
    }

    public void mountVehicle(Mount mount){
        this.mount = mount;
        mount.setOrientation(getOrientation());
        speed.increaseSpeed(mount.getMovementSpeed());
        // notifyObservers(); Only if we want the sprite to change to a mounted sprite
    }

    public void dismountVehicle(){
        if(mount!= null) {
            speed.decreaseSpeed(mount.getMovementSpeed());
            this.mount = null;
            // notifyObservers(); Only if we want the sprite to change
        }
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

    public void addWeaponSkills(Skill... weaponSkills) {
        for(Skill weaponSkill: weaponSkills) {
            if(!this.weaponSkills.contains(weaponSkill)) {
                this.weaponSkills.add(weaponSkill);
                assignStartingLevelToSkill(weaponSkill);
            }
        }
    }

    public void addNonWeaponSkills(Skill... nonWeaponSkills) {
        for(Skill nonWeaponSkill: nonWeaponSkills) {
            if(!this.nonWeaponSkills.contains(nonWeaponSkill)) {
                this.nonWeaponSkills.add(nonWeaponSkill);
                assignStartingLevelToSkill(nonWeaponSkill);
            }
        }
    }

    private void assignStartingLevelToSkill(Skill skill) {
        if (!skillLevelsMap.containsKey(skill)) {
            skillLevelsMap.put(skill, new SkillLevel(1));
        }
    }

    public int getSkillLevel(Skill skill) {
        if (skillLevelsMap.containsKey(skill)) {
            return skillLevelsMap.get(skill).getSkillLevel();
        } else
            return 0;
    }

    public void setSkillLevel(Skill skill, int skillLevel) {
        if (skillLevelsMap.containsKey(skill)) {
            skillLevelsMap.get(skill).setSkillLevel(skillLevel);
        }
    }

    public WeaponItem getWeaponItem() {
        return equipment.getEquippedWeapon();
    }
    public ArmorItem getArmorItem() { return equipment.getEquippedArmor(); }
    public RingItem getRingItem() { return equipment.getEquippedRing(); }

    public void attack() {
        if(equipment.hasWeapon()) {
            getWeaponItem().attack(this);
        }
    }

    public void addItemToHotBar(TakeableItem takeableItem, int index){
        hotBar.addItem(takeableItem, index);
    }

    public void useHotBar(int index){
        hotBar.use(index);
    }

    public void useItem() {
        hotBar.use(currentlySelectedItem);
        hotBar.removeItem(currentlySelectedItem);
    }

    public void useSkill(int index){
        if(nonWeaponSkills.size() - 1 < index || index < 0) return;
        else{
            nonWeaponSkills.get(index).fire(this);
        }
    }

    public void  useSkill(Skill skill){
        for (int i = 0; i < nonWeaponSkills.size(); ++i){
            Skill s = nonWeaponSkills.get(i);
            if (skill.equals(s)){
                s.fire(this);
            }
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

    public void scrollUp() {
        if(currentlySelectedItem <= 0) { currentlySelectedItem = hotBar.getSize()-1; }
        else { currentlySelectedItem--; }
    }

    public void scrollDown() {
        if(currentlySelectedItem >= hotBar.getSize()-1) { currentlySelectedItem = 0; }
        else { currentlySelectedItem++; }
    }

    public int getCurrentlySelectedItemIndex() {
        return currentlySelectedItem;
    }

    public int getCurrentlySelectedSkillIndex() {
        return currentlySelectedSkill;
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

    public String getRandomFacts(int observeStrength) {
        Random random = new Random();

        int randomAttribute = random.nextInt(6);

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
                int sightRadGuess = getSight() + (error / 10 * (random.nextInt(3)) - 1);
                return "Current Sight Radius: " + sightRadGuess;
            case 4:
                int levelGuess = getLevel() + (error / 10 * (random.nextInt(3)) - 1);
                return "Current XP Level: " + levelGuess;
            default: return "Nothing to report!";
        }
    }

    public int getCurrentMana() {
        return mana.getCurrentMana();
    }

    public int getCurrentGold() {
        return gold.getGoldAmount();
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

    public void accept(SavingVisitor visitor) {
        visitor.visitEntity(this);
    }

    public int getExperience() {
        return xpLevel.getExperience();
    }

    public int getExperienceToNextLevel() {
        return xpLevel.getExpToNextLevel();
    }

    public int getManaPoints() {
        return mana.getCurrentMana();
    }

    public int getMaxMana() {
        return mana.getMaxMana();
    }

    public long getSpeed() {
        return speed.getSpeed();
    }

    public int getMaxGold() {
        return gold.getMaxGold();
    }

    public int getAttackPoints() {
        return attack.getAttackPoints();
    }

    public int getAttackModifier() {
        return attack.getModifier();
    }

    public int getDefensePoints() {
        return defense.getDefensePoints();
    }

    public int getDefenseModifier() {
        return defense.getModifier();
    }

    public void increaseDefense(int amount) {
        defense.increaseDefensePoints(amount);
    }

    public void decreaseDefense(int amount) {
        defense.decreaseDefensePoints(amount);
    }

    public Mount getMount() {
        return mount;
    }

    public List<Terrain> getPassableTerrains() {
        return compatableTerrain;
    }

    public List<Skill> getWeaponSkills() {
        return weaponSkills;
    }

    public List<Skill> getNonWeaponSkills() {
        return nonWeaponSkills;
    }

    public ItemHotBar getItemHotBar() {
        return hotBar;
    }

    public void setSpeed(long speed) {
        this.speed.setSpeed(speed);
    }

    public void setNoise(int noise) {
        noiseLevel.setNoise(noise);
    }

    public boolean hasItems() {
        return inventory.size() > 0;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void reset() {
        health.refill();
        mana.refill();
    }

    public void addItemsToInventory(Inventory inv) {
        inventory.addInventory(inv);
    }

    public void regenerateMana() {
        mana.regenerate();
    }

    public int getSkillPoints() {
        return xpLevel.getPointsAvailable();
    }

    public void setSkillPointsAvaiable(int amount){
        xpLevel.setPointsAvailable(amount);
    }

    public int getManaRegenRate() {
        return mana.getManaRegenRate();
    }

    public int getTraversalStrength() {
        return traversalStrength;
    }

    public void setTraversalStrength(int traversalStrength) {
        this.traversalStrength = traversalStrength;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasItemInInventory(String itemName) {
        return inventory.hasItem(itemName);
    }
}

