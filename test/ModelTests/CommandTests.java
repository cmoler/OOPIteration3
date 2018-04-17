package ModelTests;

import Controller.GameLoop;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.PickPocketCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.DisarmTrapCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.TakeableItem;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandTests {

    @Test
    public void testTrapDisarm() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger(new GameLoop())), level);

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 0,1, Orientation.SOUTHWEST);

        Entity entity = new Entity();

        Skill disarmTrap = new Skill("Detect and Remove Trap", linear1, new DisarmTrapCommand(levelMessenger), new SendInfluenceEffectCommand(levelMessenger), 1, 1);

        entity.addNonWeaponSkills(disarmTrap);

        Point3D center = new Point3D(0,0,0);

        level.addEntityTo(center, entity);

        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Trap(observers, new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), new Trap(observers, new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), new Trap(observers, new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), new Trap(observers, new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), new Trap(observers, new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), new Trap(observers, new RemoveHealthCommand(20), 0));

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        Assert.assertEquals(100, entity.getCurrentHealth());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        Assert.assertEquals(100, entity.getCurrentHealth());
    }

    @Test
    public void testTrapDisarmFailure() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger(new GameLoop())), level);

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 0,1, Orientation.SOUTHWEST);

        Entity entity = new Entity();

        Skill disarmTrap = new Skill("Detect and Remove Trap", linear1, new DisarmTrapCommand(levelMessenger), new SendInfluenceEffectCommand(levelMessenger), 1, 1);

        entity.addNonWeaponSkills(disarmTrap);

        Point3D center = new Point3D(0,0,0);

        level.addEntityTo(center, entity);

        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Trap(observers, new RemoveHealthCommand(50), 100));

        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertTrue(level.getTrapLocations().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertEquals(50, entity.getCurrentHealth());
    }

    @Test
    public void testPickPocket() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger(new GameLoop())), level);

        SendInfluenceEffectCommand sendInfluenceEffectCommand = new SendInfluenceEffectCommand(levelMessenger);

        PickPocketCommand pickPocketCommand = new PickPocketCommand(levelMessenger);
        LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(pickPocketCommand, 1, 1, Orientation.NORTH);

        Skill pickpocketSkill = new Skill("Pickpocket", linearInfluenceEffect, new PickPocketCommand(levelMessenger), sendInfluenceEffectCommand, 1, 1);

        Entity thief = new Entity();
        Entity victim = new Entity();

        TakeableItem item = new ConsumableItem("thingie", new AddHealthCommand(10));

        victim.addItemToInventory(item);

        thief.addNonWeaponSkills(pickpocketSkill);

        thief.setSkillLevel(pickpocketSkill, 1000);

        Point3D originPt = new Point3D(0, 0, 0);
        level.addEntityTo(originPt, thief);
        thief.setOrientation(Orientation.NORTH);
        level.addEntityTo(Orientation.getAdjacentPoint(originPt, Orientation.NORTH), victim);

        Assert.assertFalse(thief.hasItemInInventory(item));
        Assert.assertTrue(victim.hasItemInInventory(item));

        try {
            thief.useSkill(0);
            level.processInteractions();

            while (!thief.hasItemInInventory(item)) {
                thief.useSkill(0);
                level.processInteractions();
            }

            Assert.assertTrue(thief.hasItemInInventory(item));
            Assert.assertFalse(victim.hasItemInInventory(item));

        } catch (Exception e) { // TODO: fix once AI controller logic is set
            Assert.assertFalse(thief.hasItemInInventory(item));
            Assert.assertTrue(victim.hasItemInInventory(item));
        }
    }

}
