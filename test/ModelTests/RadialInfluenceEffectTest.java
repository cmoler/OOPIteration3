package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Level.Trap;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RadialInfluenceEffectTest {

    // TODO: test for disarming trap

    @Test
    public void RadialInfluenceEffectTest() {
        Command damageCommand = new RemoveHealthCommand(15);

        List<LevelViewElement> observers = new ArrayList<>();

        RadialInfluenceEffect influenceEffect = new RadialInfluenceEffect(damageCommand, 3, 5, Orientation.NORTH);

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Assert.assertEquals(entity1.getMaxHealth(), entity1.getCurrentHealth(), 0);
        Assert.assertEquals(entity2.getMaxHealth(), entity2.getCurrentHealth(), 0);

        influenceEffect.hitEntity(entity1);

        Assert.assertEquals(entity1.getCurrentHealth(), 85, 0);
        //Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Point3D startPoint = new Point3D(0, 0, 0);

        influenceEffect.nextMove(startPoint);
        influenceEffect.nextMove(startPoint);
        ArrayList<Point3D> adjacentPoints = influenceEffect.nextMove(startPoint);
        Point3D expectedPoint = new Point3D(5, 6, 4);

        for(int i = 0; i < adjacentPoints.size(); i++) {
            System.out.println("RADIAL POINTS: "+adjacentPoints.get(i).toString());
        }
        Assert.assertEquals(adjacentPoints.size(), 12, 0);
    }
}
