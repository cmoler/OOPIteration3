package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.AddHealthCommand;
import Model.Command.EntityCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.InteractiveItem;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ItemTests {

    private Level level;
    private Entity entity;
    private List<LevelViewElement> observers;

    @Before
    public void init() {
        observers = new ArrayList<>();
        level = new Level(observers);
        entity = new Entity();
    }

    @Test
    public void testOneShotItemInteractions() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

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

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertFalse(level.hasItem(oneShotItem));
    }

    @Test
    public void testInteractiveItemInteractions() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

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

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 60, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));
    }

    @Test
    public void testWeaponEquip() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger()), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        SettableCommand heal = new AddHealthCommand(20);
        WeaponItem weapon = new WeaponItem("weapon", heal);
        weapon.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), weapon);

        Assert.assertFalse(entity.hasItemInInventory(weapon));

        level.processInteractions();

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
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger()), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ToggleableCommand heal = new ToggleHealthCommand(20);
        ArmorItem armor = new ArmorItem("armor", heal);
        armor.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), armor);

        Assert.assertFalse(entity.hasItemInInventory(armor));

        level.processInteractions();

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
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger()), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ToggleableCommand heal = new ToggleHealthCommand(20);
        RingItem ring = new RingItem("ring", heal);
        ring.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), ring);

        Assert.assertFalse(entity.hasItemInInventory(ring));

        level.processInteractions();

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

        entity.addSkillsToMap(oneHand);

        WeaponItem equippableSword = new WeaponItem("Sword", new AddHealthCommand(20));
        equippableSword.setSkill(oneHand);
        equippableSword.onTouch(entity);

        WeaponItem nonEquippableSword = new WeaponItem("Sword", new AddHealthCommand(20));
        nonEquippableSword.setSkill(new Skill());
        nonEquippableSword.onTouch(entity);

        equippableSword.select();
        assertTrue(entity.getWeaponItem() == equippableSword);

        nonEquippableSword.select();
        assertTrue(entity.getWeaponItem() == equippableSword);
    }
}
