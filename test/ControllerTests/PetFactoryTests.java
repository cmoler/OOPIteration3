package ControllerTests;

import Controller.Factories.EntityFactories.PetFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertTrue;

public class PetFactoryTests {

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
    public void testBuildPet(){
        PetFactory petFactory = new PetFactory(skillsFactory);

        Entity pet = petFactory.buildEntity();

        assertTrue(pet != null);
    }

    @Test
    public void testBuildPetWithInventory(){
        PetFactory petFactory = new PetFactory(skillsFactory);

        ArmorItem item = new ArmorItem(null, null);

        Entity pet = petFactory.buildEntity(item);

        assertTrue(pet != null);
        assertTrue(pet.hasItemInInventory(item));
    }

}
