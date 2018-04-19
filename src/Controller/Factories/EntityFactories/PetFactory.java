package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

public class PetFactory extends EntityFactory {

    public PetFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) { // TODO: add more skills for pets
        Entity pet = new Entity();

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                pet.addItemToInventory(items[i]);
            }
        }

        pet.addNonWeaponSkills(
                getSkillsFactory().getPickpocket()
        );

        return pet;
    }

}
