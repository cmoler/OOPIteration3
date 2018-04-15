package Controller.Visitor;

import Model.AI.ConfusedAI;
import Model.AI.FriendlyAI;
import Model.AI.FrozenAI;
import Model.AI.HostileAI;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Equipment;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.Item;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;

public class SavingVisitor implements Visitor {

    @Override
    public void visitGameModel(GameModel gameModel) {

    }

    @Override
    public void visitLevel(Level level) {

    }

    @Override
    public void visitEntity(Entity entity) {

    }

    @Override
    public void visitEquipment(Equipment equipment) {

    }

    @Override
    public void visitInventory(Inventory inventory) {

    }

    @Override
    public void visitInfiniteAreaEffect(InfiniteAreaEffect infiniteAreaEffect) {

    }

    @Override
    public void visitOneShotAreaEffect(OneShotAreaEffect oneShotAreaEffect) {

    }

    @Override
    public void visitMount(Mount mount) {

    }

    @Override
    public void visitItem(Item item) {

    }

    @Override
    public void visitInteractiveItem(Item item) {

    }

    @Override
    public void visitOneShot(Item item) {

    }

    @Override
    public void visitWeaponItem(WeaponItem weaponItem) {

    }

    @Override
    public void visitArmorItem(ArmorItem armorItem) {

    }

    @Override
    public void visitRingItem(RingItem ringItem) {

    }

    @Override
    public void visitConsumableItem(ConsumableItem consumableItem) {

    }

    @Override
    public void visitTrap(Trap trap) {

    }

    @Override
    public void visitRiver(River river) {

    }

    @Override
    public void visitTerrain(Terrain terrain) {

    }

    @Override
    public void visitDecal(Decal decal) {

    }

    @Override
    public void visitConfusedAI(ConfusedAI confusedAI) {

    }

    @Override
    public void visitFriendlyAI(FriendlyAI friendlyAI) {

    }

    @Override
    public void visitHostileAI(HostileAI hostileAI) {

    }

    @Override
    public void visitFrozenAI(FrozenAI frozenAI) {

    }
}
