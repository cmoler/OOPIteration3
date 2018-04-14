package ModelTests;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Level.Level;
import Model.Level.Trap;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

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

        //Influence effect tests
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(damageCommand, 5, 5, Orientation.NORTH);
        Entity entity3 = new Entity();

        level.addInfluenceEffectTo(new Point3D(-2, 0, 2), influenceEffect);
        level.addEntityTo(new Point3D(-2, 2, 0), entity3);

        level.processInteractions();

        assertEquals(100, entity3.getCurrentHealth(), 0);

        level.processInteractions();

        assertEquals(85, entity3.getCurrentHealth(), 0);


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
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity);
        assertEquals(new Point3D(0,0,0), level.getEntityPoint(entity));
    }
}
