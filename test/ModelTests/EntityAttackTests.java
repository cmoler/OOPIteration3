package ModelTests;

import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EntityAttackTests {

    private Entity entity;
    private Entity affectedEntity;
    private Level level;
    private LevelMessenger levelMessenger;

    @Before
    public void init() {
        level = new Level(new ArrayList<>());
        levelMessenger = new LevelMessenger(null, level);
        entity = new Entity();
        affectedEntity = new Entity();
        entity.setOrientation(Orientation.SOUTHWEST);

        level.addEntityTo(new Point3D(0,0,0), entity);
        level.addEntityTo(new Point3D(-1, 0, 1), affectedEntity);
    }

    @Test
    public void entityFiresAttackTest() {
        Skill swordSkill = new Skill("Sword Skill", null, null, new SendInfluenceEffectCommand(levelMessenger), 1, 0);
        WeaponItem sword = new WeaponItem("Sword", new RemoveHealthCommand(20), null, swordSkill,
                new LinearInfluenceEffect(new RemoveHealthCommand(10), 1,0, entity.getOrientation()), 10,
                10, 10, 10, 0);

        entity.addWeaponSkills(swordSkill);
        entity.equipWeapon(sword);
        entity.attack();

        level.processInteractions();
        System.out.println(affectedEntity.getCurrentHealth());
    }
}
