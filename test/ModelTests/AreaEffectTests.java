package ModelTests;

import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.InstaDeathCommand;
import Model.Command.EntityCommand.LevelUpCommand;
import Model.Command.EntityCommand.SettableEntityCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableEntityCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import org.junit.Test;
import org.junit.Assert;

public class AreaEffectTests {

    @Test
    public void testOneShotLevelUp() {
        Entity entity = new Entity();

        Command levelUpCommand = new LevelUpCommand();

        OneShotAreaEffect oneshot = new OneShotAreaEffect(levelUpCommand);

        Assert.assertEquals(0, entity.getLevel(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(1, entity.getLevel(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(1, entity.getLevel(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(1, entity.getLevel(), 0);
    }

    @Test
    public void testOneShotDamage() {
        Entity entity = new Entity();

        int amount = 20;

        Command damageCommand = new RemoveHealthCommand(amount);

        OneShotAreaEffect oneshot = new OneShotAreaEffect(damageCommand);

        Assert.assertEquals(entity.getMaxHealth(), entity.getCurrentHealth(), 0);
        Assert.assertEquals(entity.getMaxHealth(), 100, 0);

        oneshot.trigger(entity);
        Assert.assertEquals(entity.getMaxHealth() - amount, entity.getCurrentHealth(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(entity.getMaxHealth() - amount, entity.getCurrentHealth(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(entity.getMaxHealth() - amount, entity.getCurrentHealth(), 0);

        Assert.assertEquals(entity.getMaxHealth(), 100, 0);
        Assert.assertEquals(80, entity.getCurrentHealth(), 0);
    }

    @Test
    public void testOneShotHeal() {
        Entity entity = new Entity();

        int healAmount = 13;
        Command healthCommand = new AddHealthCommand(healAmount);

        int damageAmount = 50;
        entity.decreaseHealth(damageAmount);

        Assert.assertEquals(entity.getMaxHealth(), 100, 0);
        Assert.assertEquals(entity.getCurrentHealth(), 50, 0);

        OneShotAreaEffect oneshot = new OneShotAreaEffect(healthCommand);

        oneshot.trigger(entity);
        Assert.assertEquals(63, entity.getCurrentHealth(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(63, entity.getCurrentHealth(), 0);

        oneshot.trigger(entity);
        Assert.assertEquals(63, entity.getCurrentHealth(), 0);

        Assert.assertEquals(100, entity.getMaxHealth(), 0);
    }

    @Test
    public void testInfiniteLevelUp() {
        Entity entity = new Entity();

        Command levelUpCommand = new LevelUpCommand();

        InfiniteAreaEffect infinite = new InfiniteAreaEffect(levelUpCommand);

        Assert.assertEquals(0, entity.getLevel(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(1, entity.getLevel(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(2, entity.getLevel(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(3, entity.getLevel(), 0);
    }

    @Test
    public void testInfniteDamage() {
        Entity entity = new Entity();

        int amount = 20;

        Command damageCommand = new RemoveHealthCommand(amount);

        InfiniteAreaEffect infinite = new InfiniteAreaEffect(damageCommand);

        Assert.assertEquals(entity.getMaxHealth(), entity.getCurrentHealth(), 0);
        Assert.assertEquals(entity.getMaxHealth(), 100, 0);

        infinite.trigger(entity);
        Assert.assertEquals(80, entity.getCurrentHealth(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(60, entity.getCurrentHealth(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(40, entity.getCurrentHealth(), 0);

        Assert.assertEquals(entity.getMaxHealth(), 100, 0);
    }

    @Test
    public void testInfniteHeal() {
        Entity entity = new Entity();

        int healAmount = 5;
        Command healthCommand = new AddHealthCommand(healAmount);

        int damageAmount = 50;
        entity.decreaseHealth(damageAmount);

        Assert.assertEquals(entity.getMaxHealth(), 100, 0);
        Assert.assertEquals(entity.getCurrentHealth(), 50, 0);

        InfiniteAreaEffect infinite = new InfiniteAreaEffect(healthCommand);

        infinite.trigger(entity);
        Assert.assertEquals(55, entity.getCurrentHealth(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(60, entity.getCurrentHealth(), 0);

        infinite.trigger(entity);
        Assert.assertEquals(65, entity.getCurrentHealth(), 0);

        Assert.assertEquals(100, entity.getMaxHealth(), 0);
    }

    @Test
    public void testOneShotInstaDeath() {
        Entity entity = new Entity();
        Entity otherEntity = new Entity();

        Assert.assertFalse(entity.isDead());
        Assert.assertFalse(otherEntity.isDead());

        Command insteaDeathCommand = new InstaDeathCommand();

        OneShotAreaEffect oneshot = new OneShotAreaEffect(insteaDeathCommand);

        oneshot.trigger(entity);
        oneshot.trigger(otherEntity);

        Assert.assertTrue(entity.isDead());
        Assert.assertFalse(otherEntity.isDead());
    }

    @Test
    public void testInfiniteInstaDeath() {
        Entity entity = new Entity();
        Entity otherEntity = new Entity();

        Assert.assertFalse(entity.isDead());
        Assert.assertFalse(otherEntity.isDead());

        Command insteaDeathCommand = new InstaDeathCommand();

        InfiniteAreaEffect infinite = new InfiniteAreaEffect(insteaDeathCommand);

        infinite.trigger(entity);
        infinite.trigger(otherEntity);

        Assert.assertTrue(entity.isDead());
        Assert.assertTrue(otherEntity.isDead());
    }
}
