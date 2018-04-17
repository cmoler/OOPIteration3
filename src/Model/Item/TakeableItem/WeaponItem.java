package Model.Item.TakeableItem;

import Model.Command.Command;
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

    public WeaponItem(String name, Command command, WeaponEquipStrategy weaponEquipStrategy, Skill hostSKill,
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

    public WeaponItem(String name, Command command) {
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
        SkillLevel skillLevel = entity.getSkillLevel(hostSKill);

        if(skillLevel != null) {
            //TODO: figure out what else to put here
            // TODO: figure out how to get skills to modify stats for stuff like attacks
            //int modifier = skillLevel.getSkillLevel();
            //int damage = (attackDamage * modifier) / accuracy;
            hostSKill.setInfluence(influenceEffect);
            hostSKill.setBehavior(getCommand());
            hostSKill.fire(entity);
        }
    }

    @Override
    public void select() {
        weaponEquipStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItemInInventory(this)) {
            weaponEquipStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }
}
