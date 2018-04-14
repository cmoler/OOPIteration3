package Model.Item.TakeableItem;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.TakeableItem.InventoryStrategy.WeaponEquipStrategy;

public class WeaponItem extends TakeableItem{

    private WeaponEquipStrategy inventoryStrategy;
    private Skill hostSKill;
    private InfluenceEffect influenceEffect;
    private int attackDamage;
    private int attackSpeed;
    private int accuracy;
    private int useCost;

    protected WeaponItem(String name, Command command) {
        super(name, command);
    }

    @Override
    public void select() {

    }

    @Override
    public void onTouch(Entity entity) {

    }
}
