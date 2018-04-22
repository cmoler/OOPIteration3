package ModelTests;

import Controller.GameLoop;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.InteractiveItem;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTests {

    private Level level;
    private Entity entity;

    private GameLoop gameLoop;
    private GameLoopMessenger messenger;

    @Before
    public void init() {
        level = new Level();
        entity = new Entity();

        gameLoop = new GameLoop();
        messenger = new GameLoopMessenger(gameLoop);
    }

    @Test
    public void testOneShotItemInteractions() {
        Level level = new Level();

        Command damageCommand = new RemoveHealthCommand(20);

        OneShotItem oneShotItem = new OneShotItem("oneshot", damageCommand);

        Assert.assertFalse(level.hasItem(oneShotItem));

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity1);
        level.addEntityTo(new Point3D(1, 0,0 ), entity2);

        level.addItemnTo(new Point3D(0, 0,0), oneShotItem);

        Assert.assertTrue(level.hasItem(oneShotItem));

        Assert.assertEquals(entity1.getCurrentHealth(), 100, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        level.advance();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertFalse(level.hasItem(oneShotItem));
    }

    @Test
    public void testInteractiveItemInteractions() {
        Level level = new Level();

        Command damageCommand = new RemoveHealthCommand(20);

        InteractiveItem interactiveItem = new InteractiveItem("oneshot", damageCommand);

        Assert.assertFalse(level.hasItem(interactiveItem));

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity1);
        level.addEntityTo(new Point3D(1, 0,0 ), entity2);

        level.addItemnTo(new Point3D(0, 0,0), interactiveItem);

        Assert.assertTrue(level.hasItem(interactiveItem));

        Assert.assertEquals(entity1.getCurrentHealth(), 100, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        level.advance();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));

        level.advance();

        Assert.assertEquals(entity1.getCurrentHealth(), 60, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));
    }

    @Test
    public void testItemPickup() {
        Level level = new Level();

        Command damageCommand = new RemoveHealthCommand(20);

        TakeableItem item = new ConsumableItem("oneshot", damageCommand);
        item.setCurrentLevelMessenger(new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level));

        Assert.assertFalse(level.hasItem(item));

        Entity entity1 = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity1);

        level.addItemnTo(new Point3D(0, 0,0), item);

        Assert.assertTrue(level.hasItem(item));
        Assert.assertFalse(entity1.hasItemInInventory(item));

        level.advance();

        Assert.assertFalse(level.hasItem(item));
        Assert.assertTrue(entity1.hasItemInInventory(item));

    }

    @Test
    public void testWeaponEquip() {
        Level level = new Level();

        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        SettableCommand heal = new AddHealthCommand(20);
        WeaponItem weapon = new WeaponItem("weapon", heal);
        weapon.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), weapon);

        Assert.assertFalse(entity.hasItemInInventory(weapon));

        level.advance();

        Assert.assertTrue(entity.hasItemInInventory(weapon));

        entity.equipWeapon(weapon);

        Assert.assertFalse(entity.hasItemInInventory(weapon));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.unequipWeapon();

        Assert.assertTrue(entity.hasItemInInventory(weapon));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        SettableCommand heal2 = new AddHealthCommand(55);

        WeaponItem weapon2 = new WeaponItem("weapon", heal2);

        entity.addItemToInventory(weapon2);

        Assert.assertTrue(entity.hasItemInInventory(weapon));
        Assert.assertTrue(entity.hasItemInInventory(weapon2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.equipWeapon(weapon2);

        Assert.assertTrue(entity.hasItemInInventory(weapon));
        Assert.assertFalse(entity.hasItemInInventory(weapon2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.equipWeapon(weapon);

        Assert.assertFalse(entity.hasItemInInventory(weapon));
        Assert.assertTrue(entity.hasItemInInventory(weapon2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.unequipWeapon();

        Assert.assertTrue(entity.hasItemInInventory(weapon));
        Assert.assertTrue(entity.hasItemInInventory(weapon2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.unequipWeapon();
        entity.unequipArmor();
        entity.unequipRing();
    }

    @Test
    public void testArmorEquipping() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ToggleableCommand heal = new ToggleHealthCommand(20);
        ArmorItem armor = new ArmorItem("armor", heal);
        armor.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), armor);

        Assert.assertFalse(entity.hasItemInInventory(armor));

        level.advance();

        Assert.assertTrue(entity.hasItemInInventory(armor));

        entity.equipArmor(armor);

        Assert.assertFalse(entity.hasItemInInventory(armor));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        entity.unequipArmor();

        Assert.assertTrue(entity.hasItemInInventory(armor));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        ToggleableCommand heal2 = new ToggleHealthCommand(55);

        ArmorItem armor2 = new ArmorItem("armor", heal2);

        entity.addItemToInventory(armor2);

        Assert.assertTrue(entity.hasItemInInventory(armor));
        Assert.assertTrue(entity.hasItemInInventory(armor2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.equipArmor(armor2);

        Assert.assertTrue(entity.hasItemInInventory(armor));
        Assert.assertFalse(entity.hasItemInInventory(armor2));
        Assert.assertEquals(155, entity.getMaxHealth(), 0);

        entity.equipArmor(armor);

        Assert.assertFalse(entity.hasItemInInventory(armor));
        Assert.assertTrue(entity.hasItemInInventory(armor2));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        entity.unequipArmor();

        Assert.assertTrue(entity.hasItemInInventory(armor));
        Assert.assertTrue(entity.hasItemInInventory(armor2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.unequipWeapon();
        entity.unequipArmor();
        entity.unequipRing();
    }

    @Test
    public void testRingEquipping() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ToggleableCommand heal = new ToggleHealthCommand(20);
        RingItem ring = new RingItem("ring", heal);
        ring.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), ring);

        Assert.assertFalse(entity.hasItemInInventory(ring));

        level.advance();

        Assert.assertTrue(entity.hasItemInInventory(ring));

        entity.equipRing(ring);

        Assert.assertFalse(entity.hasItemInInventory(ring));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        entity.unequipRing();

        Assert.assertTrue(entity.hasItemInInventory(ring));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        ToggleableCommand heal2 = new ToggleHealthCommand(55);

        RingItem ring2 = new RingItem("armor", heal2);

        entity.addItemToInventory(ring2);

        Assert.assertTrue(entity.hasItemInInventory(ring));
        Assert.assertTrue(entity.hasItemInInventory(ring2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.equipRing(ring2);

        Assert.assertTrue(entity.hasItemInInventory(ring));
        Assert.assertFalse(entity.hasItemInInventory(ring2));
        Assert.assertEquals(155, entity.getMaxHealth(), 0);

        entity.equipRing(ring);

        Assert.assertFalse(entity.hasItemInInventory(ring));
        Assert.assertTrue(entity.hasItemInInventory(ring2));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        entity.unequipRing();

        Assert.assertTrue(entity.hasItemInInventory(ring));
        Assert.assertTrue(entity.hasItemInInventory(ring2));
        Assert.assertEquals(100, entity.getMaxHealth(), 0);

        entity.unequipWeapon();
        entity.unequipArmor();
        entity.unequipRing();
    }

    @Test
    public void userCannotEquipItemIfSkillNotInsideTheirMapTest() {
        Skill oneHand = new Skill();

        entity.addWeaponSkills(oneHand);

        WeaponItem equippableSword = new WeaponItem("Sword", new AddHealthCommand(20));
        equippableSword.setSkill(oneHand);
        equippableSword.setCurrentLevelMessenger(new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level));
        equippableSword.onTouch(entity);

        WeaponItem nonEquippableSword = new WeaponItem("Sword", new AddHealthCommand(20));
        nonEquippableSword.setSkill(new Skill());
        nonEquippableSword.setCurrentLevelMessenger(new LevelMessenger(new GameModelMessenger(messenger, new GameModel(messenger)), level));
        nonEquippableSword.onTouch(entity);

        equippableSword.select();
        Assert.assertTrue(entity.getWeaponItem() == equippableSword);
        Assert.assertFalse(entity.hasItemInInventory(equippableSword));

        nonEquippableSword.select();
        Assert.assertTrue(entity.getWeaponItem() == equippableSword);
        Assert.assertTrue(entity.hasItemInInventory(nonEquippableSword));
    }
}
