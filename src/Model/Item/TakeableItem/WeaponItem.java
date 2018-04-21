package Model.Item.TakeableItem;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Entity.EntityAttributes.SkillLevel;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.TakeableItem.InventoryStrategy.WeaponEquipStrategy;

public class WeaponItem extends TakeableItem{

    private WeaponEquipStrategy weaponEquipStrategy;
    private Skill hostSKill;
    private InfluenceEffect influenceEffect;
    private int attackDamage;
    private int attackSpeed;
    private int accuracy;
    private int useCost;
    private int range; // I think we need this?

    public WeaponItem(String name, SettableCommand command, WeaponEquipStrategy weaponEquipStrategy, Skill hostSKill,
                      InfluenceEffect influenceEffect, int attackDamage, int attackSpeed, int accuracy, int useCost, int range) {
        super(name, command);
        this.weaponEquipStrategy = weaponEquipStrategy;
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
        int skillLevel = entity.getSkillLevel(hostSKill);

        if(skillLevel != 0) {
            //TODO: figure out what else to put here
            // TODO: figure out how to get skills to modify stats for stuff like attacks
            //int modifier = skillLevel.getSkillLevel();
            //int damage = (attackDamage * modifier) / accuracy;
            hostSKill.setInfluence(influenceEffect);
            hostSKill.setBehavior((SettableCommand) getCommand());  // TODO: is this POOP? even if it is casting, I would say that it does not violate OCP (as we know that weaponItems will always take in a SettableCommand)
            hostSKill.fire(entity);
        }
    }

    @Override
    public void select() {
        weaponEquipStrategy.useStrategy();
    }

    protected void setItemStrategyEntity(Entity entity) {
        weaponEquipStrategy.setEntity(entity);
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitItem(this);
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getAttackSpeed() {
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
}
