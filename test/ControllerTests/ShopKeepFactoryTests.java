package ControllerTests;

import Controller.Factories.EntityFactories.ShopKeeperFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class ShopKeepFactoryTests {

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
    public void testBuildPet(){
        ShopKeeperFactory shopKeeperFactory = new ShopKeeperFactory(skillsFactory);

        Entity shopKeep = shopKeeperFactory.buildEntity();

        assertTrue(shopKeep != null);
    }

    @Test
    public void testBuildPetWithInventory(){
        ShopKeeperFactory shopKeeperFactory = new ShopKeeperFactory(skillsFactory);

        ArmorItem item = new ArmorItem(null, null);

        Entity shopKeep = shopKeeperFactory.buildEntity(item);

        assertTrue(shopKeep != null);
        assertTrue(shopKeep.hasItemInInventory(item));
    }

}
