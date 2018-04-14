package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Level.Trap;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LinearInfluenceEffectTest {

    // TODO: test for disarming trap

    @Test
    public void LinearInfluenceEffectTest() {
        Command damageCommand = new RemoveHealthCommand(15);

        List<LevelViewElement> observers = new ArrayList<>();

        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(damageCommand, 4, 5, Orientation.NORTH);

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Assert.assertEquals(entity1.getMaxHealth(), entity1.getCurrentHealth(), 0);
        Assert.assertEquals(entity2.getMaxHealth(), entity2.getCurrentHealth(), 0);



        influenceEffect.hitEntity(entity1);


        Assert.assertEquals(entity1.getCurrentHealth(), 85, 0);
        //Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Point3D startPoint = new Point3D(5, 5, 5);
        Point3D adjacentPoint = influenceEffect.nextMove(startPoint).get(0);
        Point3D expectedPoint = new Point3D(5, 6, 4);

        Assert.assertEquals(adjacentPoint.equals(expectedPoint), true);

    }
}