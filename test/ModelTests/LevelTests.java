package ModelTests;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelTests {

    @Test
    public void testAreaEffectInteractions() {

        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Command damageCommand = new RemoveHealthCommand(15);

        AreaEffect infiniteAreaEffect = new InfiniteAreaEffect(damageCommand);
        AreaEffect oneshotAreaEffect = new OneShotAreaEffect(damageCommand);

        level.addEntityTo(new Point3D(0 ,0 ,0), entity1);
        level.addEntityTo(new Point3D(1, 0 ,0), entity2);

        Assert.assertEquals(100, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        Assert.assertEquals(100, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);

        level.addAreaEffectTo(new Point3D(0,0 ,0), infiniteAreaEffect);
        level.addAreaEffectTo(new Point3D(1,0 ,0), oneshotAreaEffect);

        level.processInteractions();

        Assert.assertEquals(85, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(85, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        Assert.assertEquals(70, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(85, entity2.getCurrentHealth(), 0);
    }

    @Test
    public void testMovementInteractions(){
        Map<Point3D,Terrain> terrainLocations = new HashMap<Point3D, Terrain>();
        Map<Point3D, Obstacle> obstacleLocations = new HashMap<Point3D, Obstacle>();
        Map<Point3D, Entity> entityLocations = new HashMap<Point3D, Entity>();
        Map<Point3D, Mount> mountLocations = new HashMap<Point3D, Mount>();

        MovementHandler MH = new MovementHandler(terrainLocations,obstacleLocations,entityLocations,mountLocations);

        // Case 1: Attempting to move onto an impassable Terrain
        terrainLocations.put(new Point3D(2,2,2),Terrain.WATER);

        Entity ent = new Entity();
        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.put(new Point3D(1,2,2), ent);

        MH.processMoves();
        Assert.assertTrue(entityLocations.containsKey(new Point3D(1,2,2)));

        entityLocations.clear();
        terrainLocations.clear();

        // Case 2: Attempting to move onto an obstacle
        terrainLocations.put(new Point3D(2,2,2),Terrain.GRASS);
        obstacleLocations.put(new Point3D(2,2,2),new Obstacle());

        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.put(new Point3D(1,2,2), ent);

        MH.processMoves();
        Assert.assertTrue(entityLocations.containsKey(new Point3D(1,2,2)));

        entityLocations.clear();
        obstacleLocations.clear();

        // Case 3: Attempting to move on another entity
        Entity ent1 = new Entity();
        entityLocations.put(new Point3D(2,2,2), ent1);

        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.put(new Point3D(1,2,2), ent);

        MH.processMoves();
        Assert.assertTrue(entityLocations.get(new Point3D(1,2,2)).equals(ent));
        Assert.assertEquals(new Vec3d(0, 0, 0),ent.getVelocity());

        entityLocations.clear();

       // Case 4: Normal Movement

        terrainLocations.put(new Point3D(2,2,2),Terrain.GRASS);

        ent.setVelocity(new Vec3d(1,0,0));
        entityLocations.put(new Point3D(1,2,2), ent);

        Assert.assertTrue(ent.isMoving());

        MH.processMoves();
        Assert.assertTrue(entityLocations.get(new Point3D(2,2,2)).equals(ent));
        Assert.assertEquals(new Vec3d(0, 0, 0),ent.getVelocity());

        entityLocations.clear();

    }

    @Test
    public void testTrapInteractions() {

        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Command damageCommand = new RemoveHealthCommand(15);

        Trap trap = new Trap(observers, damageCommand);

        level.addEntityTo(new Point3D(0 ,0 ,0), entity1);
        level.addEntityTo(new Point3D(1, 0 ,0), entity2);

        Assert.assertEquals(100, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        Assert.assertEquals(100, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);

        level.addTrapTo(new Point3D(0,0 ,0), trap);

        level.processInteractions();

        Assert.assertEquals(85, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);

        level.processInteractions();

        Assert.assertEquals(85, entity1.getCurrentHealth(), 0);
        Assert.assertEquals(100, entity2.getCurrentHealth(), 0);
    }
}
