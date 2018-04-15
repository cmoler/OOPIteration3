package Controller.Factories;

import Model.AI.AIController;
import Model.AI.PetAI;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

public class PetFactory extends EntityFactory {

    private AIController controller;

    public PetFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) {
        Entity pet = new Entity();

        controller = new AIController();
        controller.setActiveState(new PetAI(pet));

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                pet.addItemToInventory(items[i]);
            }
        }

        pet.addSkillsToMap(
                getSkillsFactory().getPickpocket()
        );

        pet.addNonWeaponSkills(
                getSkillsFactory().getPickpocket()
        );

        return pet;
    }

    public AIController getController(){
        return controller;
    }
}
