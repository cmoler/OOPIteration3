package ModelTests;

import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Command.LevelCommand.DisarmTrapCommand;
import Model.Command.LevelCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Level.*;
import View.LevelView.LevelView;
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
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger()), level);

        InfluenceEffect linear1 = new LinearInfluenceEffect(new RemoveHealthCommand(10), 0,1, Orientation.SOUTHWEST);

        Entity entity = new Entity();

        Skill disarmTrap = new Skill("Detect and Remove Trap", linear1, new DisarmTrapCommand(levelMessenger), new SendInfluenceEffectCommand(levelMessenger), 1, 1);

        entity.addNonWeaponSkills(disarmTrap);

        Point3D center = new Point3D(0,0,0);

        level.addEntityTo(center, entity);

        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTH), new Trap(observers, new RemoveHealthCommand(20)));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHWEST), new Trap(observers, new RemoveHealthCommand(20)));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.NORTHEAST), new Trap(observers, new RemoveHealthCommand(20)));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTH), new Trap(observers, new RemoveHealthCommand(20)));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHWEST), new Trap(observers, new RemoveHealthCommand(20)));
        level.addTrapTo(Orientation.getAdjacentPoint(center, Orientation.SOUTHEAST), new Trap(observers, new RemoveHealthCommand(20)));

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
    }
}
