package Controller.Visitor;

import Model.AI.ConfusedAI;
import Model.AI.FriendlyAI;
import Model.AI.FrozenAI;
import Model.AI.HostileAI;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.*;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Equipment;
import Model.Entity.EntityAttributes.Inventory;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;

public interface Visitor {

    public void visitGameModel(GameModel gameModel);

    public void visitLevel(Level level);

    public void visitEntity(Entity entity);

    public void visitEquipment(Equipment equipment);

    public void visitInventory(Inventory inventory);

    public void visitInfiniteAreaEffect(InfiniteAreaEffect infiniteAreaEffect);

    public void visitOneShotAreaEffect(OneShotAreaEffect oneShotAreaEffect);

    public void visitMount(Mount mount);

    public void visitItem(Item item);

    public void visitWeaponItem(WeaponItem weaponItem);

    public void visitArmorItem(ArmorItem armorItem);

    public void visitRingItem(RingItem ringItem);

    public void visitConsumableItem(ConsumableItem consumableItem);

    public void visitTrap(Trap trap);

    public void visitRiver(River river);

    public void visitTerrain(Terrain terrain);

    public void visitDecal(Decal decal);

    public void visitConfusedAI(ConfusedAI confusedAI);

    public void visitFriendlyAI(FriendlyAI friendlyAI);

    public void visitHostileAI(HostileAI hostileAI);

    public void visitFrozenAI(FrozenAI frozenAI);

    void visitRemoveHealthCommand(RemoveHealthCommand removeHealthCommand);

    void visitLevelUpCommand(LevelUpCommand levelUpCommand);

    void visitSendInfluenceEffectCommand(SendInfluenceEffectCommand sendInfluenceEffectCommand);

    void visitDropItemCommand(DropItemCommand dropItemCommand);

    void visitTeleportEntityCommand(TeleportEntityCommand teleportEntityCommand);

    void visitDialogCommand(DialogCommand dialogCommand);

    void visitInstaDeathCommand(InstaDeathCommand instaDeathCommand);

    void visitToggleHealthCommand(ToggleHealthCommand toggleHealthCommand);

    void visitToggleManaCommand(ToggleManaCommand toggleManaCommand);

    void visitSpeedCommand(ToggleSpeedCommand toggleSpeedCommand);

    void visitToggleSneaking(ToggleSneaking toggleSneaking);

    void visitAddHealthCommand(AddHealthCommand addHealthCommand);

    void visitBarterCommand(BarterCommand barterCommand);

    void visitConfuseEntityCommand(ConfuseEntityCommand confuseEntityCommand);

    void visitDisarmTrapCommand(DisarmTrapCommand disarmTrapCommand);

    void visitFreezeEntityCommand(FreezeEntityCommand freezeEntityCommand);

    void visitObserveEntityCommand(ObserveEntityCommand observeEntityCommand);

    void visitPickPocketCommand(PickPocketCommand pickPocketCommand);

    void visitSlowEntityCommand(SlowEntityCommand slowEntityCommand);

    void visitInfluenceEffect(InfluenceEffect linearInfluenceEffect);
}
