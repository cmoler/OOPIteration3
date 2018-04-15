package Controller.Factories;

import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

public class MonsterFactory extends EntityFactory {

    private AIController controller;

    public MonsterFactory(SkillsFactory skillsFactory){
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) {
        Entity monster = new Entity();

        controller = new AIController();
        controller.setActiveState(new HostileAI());

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                monster.addItemToInventory(items[i]);
            }
        }

        return monster;
    }

    public AIController getController(){
        return controller;
    }
}
