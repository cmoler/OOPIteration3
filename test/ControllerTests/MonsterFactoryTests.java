package ControllerTests;

import Controller.Factories.EntityFactories.MonsterFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class MonsterFactoryTests {

    private LevelMessenger levelMessenger;
    private Level level;
    private SkillsFactory skillsFactory;

    @Before
    public void before(){
        level = new Level();
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
    public void testBuildMonsterWithInventory(){
        MonsterFactory monsterFactory = new MonsterFactory(skillsFactory);

        ArmorItem item = new ArmorItem(null, null);

        Entity monster = monsterFactory.buildEntity(item);

        assertTrue(monster != null);
        assertTrue(monster.hasItemInInventory(item));
    }
}
