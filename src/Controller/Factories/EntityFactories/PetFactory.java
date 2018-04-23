package Controller.Factories.EntityFactories;

import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

import View.LevelView.EntityView.PetView;
import javafx.geometry.Point3D;


public class PetFactory extends EntityFactory {

    public PetFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) {
        Entity pet = new Entity();

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                pet.addItemToInventory(items[i]);
            }
        }

        pet.addWeaponSkills(getSkillsFactory().getOneHandedSkill());

        pet.addNonWeaponSkills(
                getSkillsFactory().getPickpocket()
        );

        return pet;
    }


    public void buildEntitySprite(Entity entity) {
        PetView petView = new PetView(entity, new Point3D(0,0,0));
    }

}
