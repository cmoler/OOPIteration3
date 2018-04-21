package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

public class ShopKeeperFactory extends EntityFactory {

    public ShopKeeperFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) { // TODO: add skills to shopkeeper
        Entity shopKeep = new Entity();

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                shopKeep.addItemToInventory(items[i]);
            }
        }

        return shopKeep;
    }

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView shopkeeperView = new EntityView(entity, new Point3D(0,0,0));
    }
}
