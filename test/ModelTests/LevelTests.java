package ModelTests;

import Controller.GameLoop;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Utility.BidiMap;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class LevelTests {

    private GameLoop gameLoop;
    private GameLoopMessenger gameLoopMessenger;

    @Before
    public void init() {
        gameLoop = new GameLoop();
        gameLoopMessenger = new GameLoopMessenger(gameLoop);
    }

    @Test
    public void testAreaEffectInteractions() {
        Level level = new Level();

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Command damageCommand = new RemoveHealthCommand(15);

        AreaEffect infiniteAreaEffect = new InfiniteAreaEffect(damageCommand);
        AreaEffect oneshotAreaEffect = new OneShotAreaEffect(damageCommand);

        level.addEntityTo(new Point3D(0 ,0 ,0), entity1);
        level.addEntityTo(new Point3D(1, 0 ,0), entity2);

        assertEquals(100, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        assertEquals(100, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);

        level.addAreaEffectTo(new Point3D(0,0 ,0), infiniteAreaEffect);
        level.addAreaEffectTo(new Point3D(1,0 ,0), oneshotAreaEffect);

        level.processInteractions();

        assertEquals(85, entity1.getCurrentHealth(), 0);
        assertEquals(85, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        assertEquals(70, entity1.getCurrentHealth(), 0);
        assertEquals(85, entity2.getCurrentHealth(), 0);
    }

    @Test
    public void testLinearInfluenceEffect() {
        Level level = new Level();

        SettableCommand damageCommand = new RemoveHealthCommand(15);

        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(damageCommand, 5, 5, Orientation.NORTH);
        Entity entity = new Entity();

        Entity entity2 = new Entity();

        Entity entity3 = new Entity();

        level.addInfluenceEffectTo(new Point3D(-2, 0, 2), influenceEffect);
        level.addEntityTo(new Point3D(-2, 2, 0), entity);
        level.addEntityTo(new Point3D(-2, 3, 0), entity2);
        level.addEntityTo(new Point3D(-2, 4, 0), entity3);

        level.processMoves();
        level.processInteractions();

        assertEquals(100, entity.getCurrentHealth(), 0);

        level.processMoves();
        level.processInteractions();

        assertEquals(95, entity.getCurrentHealth(), 0);

        level.processMoves();
        level.processInteractions();

        level.processMoves();
        level.processInteractions();

        assertEquals(95, entity.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);
        assertEquals(100, entity3.getCurrentHealth(), 0);
    }

    @Test
    public void testMovementInteractions(){
        Map<Point3D,Terrain> terrainLocations = new HashMap<Point3D, Terrain>();
        Map<Point3D, Obstacle> obstacleLocations = new HashMap<Point3D, Obstacle>();
        BidiMap<Point3D, Entity> entityLocations = new BidiMap<>();
        Map<Point3D, Mount> mountLocations = new HashMap<Point3D, Mount>();
        Map<Point3D, InfluenceEffect> influenceEffectLocations = new HashMap<Point3D, InfluenceEffect>();

        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

        MovementHandler MH = new MovementHandler(terrainLocations,obstacleLocations,entityLocations,mountLocations, influenceEffectLocations);
        MH.setDialogCommandLevelMessenger(levelMessenger);

        // Case 1: Attempting to move onto an impassable Terrain
        terrainLocations.put(new Point3D(2,2,2),Terrain.WATER);

        Entity ent = new Entity();
        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.place(new Point3D(1,2,2), ent);

        MH.processMoves();
        Assert.assertTrue(entityLocations.hasKey(new Point3D(1,2,2)));

        entityLocations.clear();
        terrainLocations.clear();

        // Case 2: Attempting to move onto an obstacle
        terrainLocations.put(new Point3D(2,2,2),Terrain.GRASS);
        obstacleLocations.put(new Point3D(2,2,2),new Obstacle());

        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.place(new Point3D(1,2,2), ent);

        MH.processMoves();
        Assert.assertTrue(entityLocations.hasKey(new Point3D(1,2,2)));

        entityLocations.clear();
        obstacleLocations.clear();

        // Case 3: Attempting to move on another entity
        Entity ent1 = new Entity();
        entityLocations.place(new Point3D(2,2,2), ent1);

        ent.setVelocity(new Vec3d(0,-1,-1));
        entityLocations.place(new Point3D(2,1,3), ent);
        ent.setOrientation(Orientation.NORTH);

        MH.processMoves();
        Assert.assertTrue(entityLocations.getValueFromKey(new Point3D(2,1,3)).equals(ent));
        Assert.assertEquals(new Vec3d(0, 0, 0),ent.getVelocity());

        entityLocations.clear();

       // Case 4: Normal Movement

        terrainLocations.put(new Point3D(2,2,2),Terrain.GRASS);

        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.place(new Point3D(1,2,2), ent);

        Assert.assertTrue(ent.isMoving());

        MH.processMoves();
        Assert.assertTrue(entityLocations.getValueFromKey(new Point3D(2,2,2)).equals(ent));
        Assert.assertEquals(new Vec3d(0, 0, 0),ent.getVelocity());

        entityLocations.clear();

    }

    @Test
    public void testTrapInteractions() {
        Level level = new Level();

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Command damageCommand = new RemoveHealthCommand(15);

        Trap trap = new Trap(damageCommand, 0);

        level.addEntityTo(new Point3D(0 ,0 ,0), entity1);
        level.addEntityTo(new Point3D(1, 0 ,0), entity2);

        assertEquals(100, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        assertEquals(100, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);

        level.addTrapTo(new Point3D(0,0 ,0), trap);

        level.processInteractions();

        assertEquals(85, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        assertEquals(85, entity1.getCurrentHealth(), 0);
        assertEquals(100, entity2.getCurrentHealth(), 0);
    }

    @Test
    public void testEntityIsFound() {
        Level level = new Level();

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity);
        assertEquals(new Point3D(0,0,0), level.getEntityPoint(entity));
    }

    @Test
    public void testInfluenceEffectCloningOnAttack() {
        SettableCommand damageCommand = new RemoveHealthCommand(20);
        SettableCommand damageCommand2 = new RemoveHealthCommand(40);

        Level level = new Level();
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(gameLoopMessenger, new GameModel(gameLoopMessenger)), level);

        Entity entity = new Entity();
        Entity dummy = new Entity();

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 5,1, Orientation.SOUTHWEST);
        InfluenceEffect linear2 = new LinearInfluenceEffect(new RemoveHealthCommand(2130), 5,1, Orientation.SOUTHEAST);

        Skill swordSkill = new Skill("Sword Skill", null, new AddHealthCommand(10), new SendInfluenceEffectCommand(levelMessenger), 1, 0);
        WeaponItem sword = new WeaponItem("Sword", damageCommand, swordSkill,
                linear1, 10, 10, 10, 10, 5);

        WeaponItem sword2 = new WeaponItem("Sword", damageCommand2, swordSkill,
                linear2, 10,10, 10, 10, 5);

        level.addEntityTo(new Point3D(0,0 ,0), entity);
        level.addEntityTo(new Point3D(0, 1, -1), dummy);

        Assert.assertEquals(100, entity.getCurrentHealth(), 0);
        Assert.assertEquals(100, dummy.getCurrentHealth(), 0);

        entity.addWeaponSkills(swordSkill);
        entity.equipWeapon(sword);
        entity.addItemToInventory(sword2);
        entity.setOrientation(Orientation.NORTH);
        entity.attack();

        level.processMoves();
        level.processInteractions();

        Assert.assertEquals(5, linear1.getMovesRemaining(), 0);

        Assert.assertEquals(100, entity.getCurrentHealth(), 0);
        Assert.assertEquals(75, dummy.getCurrentHealth(), 0);

        entity.equipWeapon(sword2);
        entity.attack();

        level.processMoves();
        level.processInteractions();

        Assert.assertEquals(5, linear1.getMovesRemaining(), 0);
        Assert.assertEquals(5, linear2.getMovesRemaining(), 0);

        Assert.assertEquals(100, entity.getCurrentHealth(), 0);
        Assert.assertEquals(30, dummy.getCurrentHealth(), 0);
    } // TODO: get view portion of influence effects working, it is hard to test where they are moving to coordinates-wise
}
