package ModelTests;

import Model.Command.LevelCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class EntityAttackTests {

    private Entity entity;
    private Level level;
    private LevelMessenger levelMessenger;

    @Before
    public void init() {
        level = new Level(new ArrayList<>());
        levelMessenger = new LevelMessenger(null, level);
        entity = new Entity();

        level.addEntityTo(new Point3D(0,0,0), entity);
    }

    @Test
    public void entityFiresAttackTest() {
        Skill swordSkill = new Skill("Sword Skill", null, null, new SendInfluenceEffectCommand(levelMessenger), 1, 0);
        WeaponItem sword = new WeaponItem("Sword", null, null, swordSkill,
                null, 10, 10, 10, 10, 0);
        entity.addSkillsToMap(swordSkill);
        entity.addWeaponSkills(swordSkill);
        entity.equipWeapon(sword);
        entity.attack();

        level.processInteractions();
        System.out.println(entity.getCurrentHealth());
    }
}
