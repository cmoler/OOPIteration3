package ControllerTests;

import Controller.Factories.MonsterFactory;
import Controller.Factories.SkillsFactory;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class MonsterFactoryTests {

    private LevelMessenger levelMessenger;
    private Level level;
    private SkillsFactory skillsFactory;

    @Before
    public void before(){
        level = new Level(new ArrayList<>());
        levelMessenger = new LevelMessenger(null, level);
        skillsFactory = new SkillsFactory(levelMessenger);
    }

    @Test
    public void testBuildMonster(){
        MonsterFactory monsterFactory = new MonsterFactory(skillsFactory);

        Entity monster = monsterFactory.buildEntity();

        assertTrue(monster != null);
    }

    @Test
    public void testControllerIsMade(){
        MonsterFactory monsterFactory = new MonsterFactory(skillsFactory);

        Entity monster = monsterFactory.buildEntity();
        AIController aiController = monsterFactory.getController();

        assertTrue(aiController != null);
    }

    @Test
    public void testControllerIsHostile(){
        MonsterFactory monsterFactory = new MonsterFactory(skillsFactory);

        Entity monster = monsterFactory.buildEntity();
        AIController aiController = monsterFactory.getController();

        assertTrue(aiController.getActiveState() instanceof HostileAI);
    }

    @Test
    public void testBuildMonsterWithInventory(){
        MonsterFactory monsterFactory = new MonsterFactory(skillsFactory);

        ArmorItem item = new ArmorItem(null, null);

        Entity monster = monsterFactory.buildEntity(item);

        assertTrue(monster != null);
        assertTrue(monster.hasItemInInventory(item));
    }
}
