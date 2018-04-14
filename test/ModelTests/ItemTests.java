package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.InteractiveItem;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
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

        Assert.assertFalse(entity.hasItem(armor));

        level.processInteractions();

        Assert.assertTrue(entity.hasItem(armor));

        entity.equipArmor(armor);

        Assert.assertFalse(entity.hasItem(armor));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        // TODO: do more tests for new armors n stuff
    }

    // TODO: NEED TESTS FOR EQUIPPING ARMORS, RINGS, WEAPONS, (USING) CONSUMABLES
    @Test
    public void userCannotEquipItemIfSkillNotInsideTheirMapTest() {
        Skill oneHand = new Skill();

        entity.addSkillsToMap(oneHand);

        WeaponItem equippableSword = new WeaponItem("Sword", new ToggleHealthCommand(20));
        equippableSword.setSkill(oneHand);
        equippableSword.onTouch(entity);

        WeaponItem nonEquippableSword = new WeaponItem("Sword", new ToggleHealthCommand(20));
        nonEquippableSword.setSkill(new Skill());
        nonEquippableSword.onTouch(entity);

        equippableSword.select();
        assertTrue(entity.getWeaponItem() == equippableSword);

        nonEquippableSword.select();
        assertTrue(entity.getWeaponItem() == equippableSword);
    }
}
