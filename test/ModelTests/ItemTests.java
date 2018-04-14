package ModelTests;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.ToggleableCommand.ToggleableCommand;
import Model.Entity.Entity;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
import Model.Level.*;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ItemTests {

    @Test
    public void testOneShotItemInteractions() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

        Command damageCommand = new RemoveHealthCommand(20);

        OneShotItem oneShotItem = new OneShotItem("oneshot", damageCommand);

        Assert.assertFalse(level.hasItem(oneShotItem));

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity1);
        level.addEntityTo(new Point3D(1, 0,0 ), entity2);

        level.addItemnTo(new Point3D(0, 0,0), oneShotItem);

        Assert.assertTrue(level.hasItem(oneShotItem));

        Assert.assertEquals(entity1.getCurrentHealth(), 100, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertFalse(level.hasItem(oneShotItem));
    }

    @Test
    public void testInteractiveItemInteractions() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);

        Command damageCommand = new RemoveHealthCommand(20);

        InteractiveItem interactiveItem = new InteractiveItem("oneshot", damageCommand);

        Assert.assertFalse(level.hasItem(interactiveItem));

        Entity entity1 = new Entity();
        Entity entity2 = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity1);
        level.addEntityTo(new Point3D(1, 0,0 ), entity2);

        level.addItemnTo(new Point3D(0, 0,0), interactiveItem);

        Assert.assertTrue(level.hasItem(interactiveItem));

        Assert.assertEquals(entity1.getCurrentHealth(), 100, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 80, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));

        level.processInteractions();

        Assert.assertEquals(entity1.getCurrentHealth(), 60, 0);
        Assert.assertEquals(entity2.getCurrentHealth(), 100, 0);

        Assert.assertTrue(level.hasItem(interactiveItem));
    }

    @Test
    public void testArmorEquipping() {
        List<LevelViewElement> observers = new ArrayList<>();

        Level level = new Level(observers);
        LevelMessenger levelMessenger = new LevelMessenger(new GameModelMessenger(new GameModel(), new GameLoopMessenger()), level);

        Entity entity = new Entity();

        level.addEntityTo(new Point3D(0, 0, 0), entity);

        ToggleableCommand heal = new ToggleHealthCommand(20);
        ArmorItem armor = new ArmorItem("armor", heal);
        armor.setCurrentLevelMessenger(levelMessenger);

        level.addItemnTo(new Point3D(0, 0,0), armor);

        Assert.assertFalse(entity.hasItem(armor));

        level.processInteractions();

        Assert.assertTrue(entity.hasItem(armor));

        entity.equipArmor(armor);

        Assert.assertFalse(entity.hasItem(armor));
        Assert.assertEquals(120, entity.getMaxHealth(), 0);

        // TODO: do more tests for new armors n stuff
    }

    // TODO: NEED TESTS FOR EQUIPPING ARMORS, RINGS, WEAPONS, (USING) CONSUMABLES
}
