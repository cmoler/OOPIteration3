package Controller.Visitor;

import Model.AI.*;
import Model.AI.PetAI.PetStates.CombatPetState;
import Model.AI.PetAI.PetStates.GeneralPetState;
import Model.AI.PetAI.PetStates.ItemPetState;
import Model.AI.PetAI.PetStates.PassivePetState;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.*;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Condition.HasItemCondition;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Equipment;
import Model.Entity.EntityAttributes.Inventory;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;

public interface Visitor {

    void visitGameModel(GameModel gameModel);

    void visitLevel(Level level);

    void visitEntity(Entity entity);

    void visitEquipment(Equipment equipment);

    void visitInventory(Inventory inventory);

    void visitInfiniteAreaEffect(InfiniteAreaEffect infiniteAreaEffect);

    void visitOneShotAreaEffect(OneShotAreaEffect oneShotAreaEffect);

    void visitMount(Mount mount);

    void visitItem(ConsumableItem item);

    void visitItem(WeaponItem item);

    void visitItem(InteractiveItem item);

    void visitItem(ArmorItem item);

    void visitItem(RingItem item);

    void visitItem(OneShotItem item);

    void visitTrap(Trap trap);

    void visitRiver(River river);

    void visitTerrain(Terrain terrain);

    void visitDecal(Decal decal);

    void visitConfusedAI(ConfusedAI confusedAI);

    void visitFriendlyAI(FriendlyAI friendlyAI);

    void visitHostileAI(HostileAI hostileAI);

    void visitFrozenAI(FrozenAI frozenAI);

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

    void visitAddManaCommand(AddManaCommand addManaCommand);

    void visitBarterCommand(BarterCommand barterCommand);

    void visitConfuseEntityCommand(ConfuseEntityCommand confuseEntityCommand);

    void visitDisarmTrapCommand(DisarmTrapCommand disarmTrapCommand);

    void visitFreezeEntityCommand(FreezeEntityCommand freezeEntityCommand);

    void visitObserveEntityCommand(ObserveEntityCommand observeEntityCommand);

    void visitPickPocketCommand(PickPocketCommand pickPocketCommand);

    void visitSlowEntityCommand(SlowEntityCommand slowEntityCommand);

    void visitInfluenceEffect(InfluenceEffect linearInfluenceEffect);

    void visitObstacle(Obstacle obstacle);

    void visitSlowedAI(SlowedAI slowedAI);

    void visitPassivePetState(PassivePetState passivePetState);

    void visitItemPetState(ItemPetState itemPetState);

    void visitGeneralPetState(GeneralPetState generalPetState);

    void visitCombatPetState(CombatPetState combatPetState);

    void visitPatrolPath(PatrolPath patrolPath);

    void visitHasItemCommand(HasItemCondition hasItemCondition);
}
