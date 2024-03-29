package Model.Item.TakeableItem;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.TakeableItem.InventoryStrategy.WeaponEquipStrategy;

public class WeaponItem extends TakeableItem{

    private WeaponEquipStrategy weaponEquipStrategy;
    private Skill hostSKill;
    private InfluenceEffect influenceEffect;
    private int attackDamage;
    private long attackSpeed;
    private int accuracy;
    private int useCost;
    private int range; // I think we need this?
    private long nextAttTime;

    public WeaponItem(String name, SettableCommand command, Skill hostSKill,
                      InfluenceEffect influenceEffect, int attackDamage, long attackSpeed, int accuracy, int useCost, int range) {
        super(name, command);
        this.weaponEquipStrategy = new WeaponEquipStrategy(this);
        this.hostSKill = hostSKill;
        this.influenceEffect = influenceEffect;
        this.attackDamage = attackDamage;
        this.attackSpeed = attackSpeed;
        this.accuracy = accuracy;
        this.useCost = useCost;
        this.range = range;

        this.influenceEffect.setCommand(command);
    }

    public WeaponItem(String name, SettableCommand command) {
        super(name, command);
        weaponEquipStrategy = new WeaponEquipStrategy(this);
    }

    public Skill getHostSKill() {
        return hostSKill;
    }

    public void setSkill(Skill skill) {
        this.hostSKill = skill;
    }

    public void attack(Entity entity) {

        if(entity.getManaPoints() > useCost && canAttack()) {

            entity.decreaseMana(useCost);

            int skillLevel = entity.getSkillLevel(hostSKill);

            if (skillLevel > 0) {
                hostSKill.setInfluence(influenceEffect);
                hostSKill.setBehavior((SettableCommand) getCommand());
                hostSKill.fire(entity);
            }

            setNextAttackTime();
        }
    }

    private boolean canAttack() {
        return System.nanoTime() > nextAttTime;
    }

    public void setNextAttackTime() {
        nextAttTime = System.nanoTime() + attackSpeed;
    }

    @Override
    public void select() {
        weaponEquipStrategy.useStrategy();
    }

    public boolean usableByEntity(Entity entity) {
        if(entity.hasSkill(getHostSKill())) {
            return true;
        }
        else return false;
    }

    public void setItemStrategyEntity(Entity entity) {
        weaponEquipStrategy.setEntity(entity);
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public long getAttackSpeed() {
        return attackSpeed;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getUseCost() {
        return useCost;
    }

    public int getRange() {
        return range;
    }

    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }

    public InfluenceEffect getInfluenceEffect() {
        return influenceEffect;
    }
}
