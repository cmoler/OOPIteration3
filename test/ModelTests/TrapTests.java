package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Level.Trap;
import View.LevelView.LevelViewElement;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TrapTests {

    @Test
    public void testTrapFiring() {
        Command damageCommand = new RemoveHealthCommand(15);

        Trap trap = new Trap(damageCommand, 0);

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        Assert.assertEquals(entity1.getMaxHealth(), entity1.getCurrentHealth(), 0);
        Assert.assertEquals(entity2.getMaxHealth(), entity2.getCurrentHealth(), 0);

        Assert.assertFalse(trap.getIsDisarmed());
        Assert.assertFalse(trap.getIsVisible());

        trap.fire(entity1);
        trap.fire(entity2);

        Assert.assertTrue(trap.getIsDisarmed());
        Assert.assertTrue(trap.getIsVisible());

        Assert.assertEquals(entity1.getCurrentHealth(), 85, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);
    }
}
