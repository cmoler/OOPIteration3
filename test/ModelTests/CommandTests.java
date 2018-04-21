package ModelTests;

import Controller.GameLoop;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.PickPocketCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.DisarmTrapCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CommandTests {

    private GameLoop gameLoop;
    private GameLoopMessenger gameLoopMessenger;

    @Before
    public void init() {
        gameLoop = new GameLoop();
        gameLoopMessenger = new GameLoopMessenger(gameLoop);
    }

    @Test
    public void testTrapDisarm() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 0,1, Orientation.SOUTHWEST);

        Entity entity = new Entity();

        Skill disarmTrap = new Skill("Detect and Remove Trap", linear1, new DisarmTrapCommand(levelMessenger), new SendInfluenceEffectCommand(levelMessenger), 1, 1);

        entity.addNonWeaponSkills(disarmTrap);

        Point3D center = new Point3D(0,0,0);

        level.addEntityTo(center, entity);

        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Trap(new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), new Trap(new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), new Trap(new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), new Trap(new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), new Trap(new RemoveHealthCommand(20), 0));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), new Trap(new RemoveHealthCommand(20), 0));

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        Assert.assertEquals(100, entity.getCurrentHealth());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTH)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST)).getIsDisarmed());

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST)).getIsDisarmed());

        Assert.assertEquals(100, entity.getCurrentHealth());
    }

    @Test
    public void testTrapDisarmFailure() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 0,1, Orientation.SOUTHWEST);

        Entity entity = new Entity();

        Skill disarmTrap = new Skill("Detect and Remove Trap", linear1, new DisarmTrapCommand(levelMessenger), new SendInfluenceEffectCommand(levelMessenger), 1, 1);

        entity.addNonWeaponSkills(disarmTrap);

        Point3D center = new Point3D(0,0,0);

        level.addEntityTo(center, entity);

        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Trap(new RemoveHealthCommand(50), 100));

        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertFalse(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        entity.useSkill(0);

        level.processMoves();
        level.processInteractions();

        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsVisible());
        Assert.assertTrue(level.getTrapMap().get(Orientation.getAdjacentPoint(center, Orientation.NORTH)).getIsDisarmed());

        Assert.assertEquals(50, entity.getCurrentHealth());
    }

    @Test
    public void testPickPocket() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

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

    @Test
    public void testTeleportCommand() {
        Level level1 = new Level();
        Level level2 = new Level();

        Entity entity = new Entity();

        GameLoop gameLoop = new GameLoop();
        GameLoopMessenger gameLoopMessenger = new GameLoopMessenger(gameLoop);

        GameModel gameModel = new GameModel(gameLoopMessenger);

        gameModel.addLevel(level2);
        gameModel.addLevel(level1);

        gameModel.setCurrentLevel(level1);

        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(gameModel.getLevelMessenger(), level2, new Point3D(1,3,2));

        AreaEffect areaEffect = new OneShotAreaEffect(teleportEntityCommand);

        level1.addEntityTo(new Point3D(0,0,0), entity);
        level1.addAreaEffectTo(new Point3D(0,0,0), areaEffect);

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), entity);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), null);

        gameModel.advance();

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), null);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), entity);
    }

    @Test
    public void testTeleportCommandOccupiedDestination() {
        Level level1 = new Level();
        Level level2 = new Level();

        Entity entity = new Entity();

        GameLoop gameLoop = new GameLoop();
        GameLoopMessenger gameLoopMessenger = new GameLoopMessenger(gameLoop);

        GameModel gameModel = new GameModel(gameLoopMessenger);

        gameModel.addLevel(level2);
        gameModel.addLevel(level1);

        gameModel.setCurrentLevel(level1);

        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(gameModel.getLevelMessenger(), level2, new Point3D(1,3,2));

        AreaEffect areaEffect = new OneShotAreaEffect(teleportEntityCommand);

        level1.addEntityTo(new Point3D(0,0,0), entity);
        level1.addAreaEffectTo(new Point3D(0,0,0), areaEffect);

        level2.addTerrainTo(new Point3D(1,3,2), Terrain.GRASS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.NORTH), Terrain.GRASS);

        Entity entity2 = new Entity();

        level2.addEntityTo(new Point3D(1,3,2), entity2);

        gameModel.setPlayer(entity);

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), entity);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), entity2);

        gameModel.advance();

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), null);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), entity);
        Assert.assertEquals(level2.getEntityAtPoint(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.NORTH)), entity2);
    }

    @Test
    public void testTeleportCommandOccupiedDestinationMoreThanOneLevel() {
        Level level1 = new Level();
        Level level2 = new Level();

        Entity entity = new Entity();

        GameLoop gameLoop = new GameLoop();
        GameLoopMessenger gameLoopMessenger = new GameLoopMessenger(gameLoop);

        GameModel gameModel = new GameModel(gameLoopMessenger);

        gameModel.addLevel(level2);
        gameModel.addLevel(level1);

        gameModel.setCurrentLevel(level1);

        TeleportEntityCommand teleportEntityCommand = new TeleportEntityCommand(gameModel.getLevelMessenger(), level2, new Point3D(1,3,2));

        AreaEffect areaEffect = new OneShotAreaEffect(teleportEntityCommand);

        level1.addEntityTo(new Point3D(0,0,0), entity);
        level1.addAreaEffectTo(new Point3D(0,0,0), areaEffect);

        level2.addTerrainTo(new Point3D(1,3,2), Terrain.GRASS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.NORTH), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.NORTHWEST), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.NORTHEAST), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.SOUTH), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.SOUTHWEST), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.SOUTHEAST), Terrain.MOUNTAINS);
        level2.addTerrainTo(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.SOUTHEAST), Orientation.SOUTHWEST), Terrain.GRASS);

        Entity entity2 = new Entity();

        level2.addEntityTo(new Point3D(1,3,2), entity2);

        gameModel.setPlayer(entity);

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), entity);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), entity2);

        gameModel.advance();

        Assert.assertEquals(level1.getEntityAtPoint(new Point3D(0,0,0)), null);
        Assert.assertEquals(level2.getEntityAtPoint(new Point3D(1,3,2)), entity);
        Assert.assertEquals(level2.getEntityAtPoint(Orientation.getAdjacentPoint(Orientation.getAdjacentPoint(new Point3D(1,3,2), Orientation.SOUTHEAST), Orientation.SOUTHWEST)), entity2);
    }

    @Test
    public void testDropItemAllPointsFree() {
        Level level = new Level();

        Entity entity = new Entity();

        GameLoop gameLoop = new GameLoop();
        GameLoopMessenger gameLoopMessenger = new GameLoopMessenger(gameLoop);

        GameModel gameModel = new GameModel(gameLoopMessenger);
        GameModelMessenger gameModelMessenger = new GameModelMessenger(gameLoopMessenger, gameModel);
        LevelMessenger messenger = new LevelMessenger(gameModelMessenger, level);

        Point3D center = new Point3D(0,0,0);

        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), Terrain.GRASS);

        ConsumableItem item = new ConsumableItem("thingie", new AddHealthCommand( 10));

        level.addEntityTo(center, entity);
        level.addItemnTo(center, item);
        item.setCurrentLevelMessenger(messenger);

        Assert.assertFalse(entity.hasItemInInventory(item));
        Assert.assertTrue(level.hasItem(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        level.processInteractions();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertFalse(level.hasItem(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        item.drop();

        Assert.assertFalse(entity.hasItemInInventory(item));
        Assert.assertTrue(level.hasItem(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        RingItem item2 = new RingItem("thingie2", new ToggleHealthCommand( 10));
        item2.setCurrentLevelMessenger(messenger);
        level.addItemnTo(center, item2);
        level.processInteractions();

        ArmorItem item3 = new ArmorItem("thingie3", new ToggleHealthCommand( 10));
        item3.setCurrentLevelMessenger(messenger);
        level.addItemnTo(center, item3);
        level.processInteractions();

        WeaponItem item4 = new WeaponItem("thingie4", new RemoveHealthCommand( 10));
        item4.setCurrentLevelMessenger(messenger);
        level.addItemnTo(center, item4);
        level.processInteractions();

        Assert.assertFalse(entity.hasItemInInventory(item));
        Assert.assertTrue(entity.hasItemInInventory(item2));
        Assert.assertTrue(entity.hasItemInInventory(item3));
        Assert.assertTrue(entity.hasItemInInventory(item4));

        Assert.assertTrue(level.hasItem(item));
        Assert.assertFalse(level.hasItem(item2));
        Assert.assertFalse(level.hasItem(item3));
        Assert.assertFalse(level.hasItem(item4));

        Assert.assertEquals(level.getEntityPoint(entity), center);

        item2.drop();
        item3.drop();
        item4.drop();

        Assert.assertFalse(entity.hasItemInInventory(item));
        Assert.assertFalse(entity.hasItemInInventory(item2));
        Assert.assertFalse(entity.hasItemInInventory(item3));
        Assert.assertFalse(entity.hasItemInInventory(item4));

        Assert.assertTrue(level.hasItem(item));
        Assert.assertTrue(level.hasItem(item2));
        Assert.assertTrue(level.hasItem(item3));
        Assert.assertTrue(level.hasItem(item4));

        Assert.assertEquals(level.getEntityPoint(entity), center);
    }

    @Test
    public void testDropItemNoPointsFree() {
        Level level = new Level();

        Entity entity = new Entity();

        GameLoop gameLoop = new GameLoop();
        GameLoopMessenger gameLoopMessenger = new GameLoopMessenger(gameLoop);

        GameModel gameModel = new GameModel(gameLoopMessenger);
        GameModelMessenger gameModelMessenger = new GameModelMessenger(gameLoopMessenger, gameModel);
        LevelMessenger messenger = new LevelMessenger(gameModelMessenger, level);

        Point3D center = new Point3D(0,0,0);

        ConsumableItem item = new ConsumableItem("thingie", new AddHealthCommand( 10));

        level.addEntityTo(center, entity);
        level.addItemnTo(center, item);
        item.setCurrentLevelMessenger(messenger);

        Assert.assertFalse(entity.hasItemInInventory(item));
        Assert.assertTrue(level.hasItem(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        level.processInteractions();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);
        Assert.assertFalse(level.hasItem(item));

        item.drop();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), Terrain.GRASS);
        level.addTerrainTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), Terrain.GRASS);

        level.addObstacleTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Obstacle());
        level.addObstacleTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), new Obstacle());
        level.addMountTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), new Mount());
        level.addEntityTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), new Entity());
        level.addEntityTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), new Entity());
        level.addItemnTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), new OneShotItem("testItem", new AddHealthCommand(10)));

        item.drop();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        RingItem item2 = new RingItem("thingie2", new ToggleHealthCommand( 10));
        item2.setCurrentLevelMessenger(messenger);

        level.addItemnTo(center, item2);
        level.processInteractions();

        ArmorItem item3 = new ArmorItem("thingie3", new ToggleHealthCommand( 10));
        item3.setCurrentLevelMessenger(messenger);

        level.addItemnTo(center, item3);
        level.processInteractions();

        WeaponItem item4 = new WeaponItem("thingie4", new RemoveHealthCommand( 10));
        item4.setCurrentLevelMessenger(messenger);

        level.addItemnTo(center, item4);
        level.processInteractions();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertTrue(entity.hasItemInInventory(item2));
        Assert.assertTrue(entity.hasItemInInventory(item3));
        Assert.assertTrue(entity.hasItemInInventory(item4));
        Assert.assertEquals(level.getEntityPoint(entity), center);

        item.drop();
        item2.drop();
        item3.drop();
        item4.drop();

        Assert.assertTrue(entity.hasItemInInventory(item));
        Assert.assertTrue(entity.hasItemInInventory(item2));
        Assert.assertTrue(entity.hasItemInInventory(item3));
        Assert.assertTrue(entity.hasItemInInventory(item4));
        Assert.assertEquals(level.getEntityPoint(entity), center);
    }

    @Test
    public void testSneakCommand() {
        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

        SendInfluenceEffectCommand sendInfluenceEffectCommand = new SendInfluenceEffectCommand(levelMessenger);

        ToggleSneaking sneakCommand = new ToggleSneaking(315);
        LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(sneakCommand, 0, 1, Orientation.NORTH);

        Skill sneakSkill = new Skill("Sneak", linearInfluenceEffect, sneakCommand, sendInfluenceEffectCommand, 1, 1);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity);

        entity.addNonWeaponSkills(sneakSkill);

        Assert.assertEquals(entity.getNoise(), 1, 0);
        Assert.assertEquals(entity.getSpeed(),1, 0);

        entity.useSkill(0);
        level.processMoves();
        level.processInteractions();
        level.processMoves();

        Assert.assertEquals(entity.getNoise(), 0, 0);
        Assert.assertEquals(entity.getSpeed(),0, 0);

        entity.useSkill(0);
        level.processMoves();
        level.processInteractions();
        level.processMoves();

        Assert.assertEquals(entity.getNoise(), 1, 0);
        Assert.assertEquals(entity.getSpeed(),1, 0);
    }
}
