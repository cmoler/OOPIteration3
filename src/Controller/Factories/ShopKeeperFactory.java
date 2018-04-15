package Controller.Factories;

import Model.AI.AIController;
import Model.AI.FriendlyAI;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;

public class ShopKeeperFactory extends  EntityFactory {

    private AIController controller;

    public ShopKeeperFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        return buildEntity(null);
    }

    public Entity buildEntity(TakeableItem... items) {
        Entity shopKeep = new Entity();

        controller = new AIController();
        controller.setActiveState(new FriendlyAI());

        if(items != null) {
            for (int i = 0; i < items.length; ++i) {
                shopKeep.addItemToInventory(items[i]);
            }
        }
        return shopKeep;
    }

    public AIController getController(){
        return controller;
    }
}
