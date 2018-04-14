package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.TakeableItem.InventoryStrategy.WeaponEquipStrategy;
import Model.Level.LevelMessenger;

public class WeaponItem extends TakeableItem{

    private WeaponEquipStrategy weaponEquipStrategy;
    private Skill hostSKill;
    private InfluenceEffect influenceEffect;
    private int attackDamage;
    private int attackSpeed;
    private int accuracy;
    private int useCost;

    protected WeaponItem(String name, Command command, LevelMessenger levelMessenger) {
        super(name, command, levelMessenger);

        weaponEquipStrategy = new WeaponEquipStrategy(this);
    }

    // TODO add "attack" function

    @Override
    public void select() {
        weaponEquipStrategy.useStrategy();
    }

    @Override
    public void onTouch(Entity entity) {
        entity.addItemToInventory(this);

        if (entity.hasItem(this)) {
            weaponEquipStrategy.setEntity(entity);
            setToBeDeleted();
        }
    }
}
