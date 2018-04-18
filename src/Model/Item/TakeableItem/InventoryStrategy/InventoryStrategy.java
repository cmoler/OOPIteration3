package Model.Item.TakeableItem.InventoryStrategy;

import Model.Entity.Entity;

public abstract class InventoryStrategy {

    private Entity entity;

    public abstract void useStrategy();

    protected Entity getEntity() {

        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
