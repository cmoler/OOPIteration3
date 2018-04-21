package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

public class MonsterFactory extends EntityFactory {

    public MonsterFactory(SkillsFactory skillsFactory){
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) { // TODO: add skills for monsters
        Entity monster = new Entity();

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                monster.addItemToInventory(items[i]);
            }
        }

        return monster;
    }

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView monsterView = new EntityView(entity, new Point3D(0,0,0));
    }
}
