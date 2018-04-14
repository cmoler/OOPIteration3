package ModelTests;

import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Level.Level;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

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
}
