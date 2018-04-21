package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

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

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView petView = new EntityView(entity, new Point3D(0,0,0));
    }
}
