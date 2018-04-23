package Model.Condition;

import Controller.Visitor.Visitor;
import Model.Entity.Entity;

public class HasItemCondition implements Condition {

    private String itemName;
    public HasItemCondition(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public boolean checkCondition(Entity entity) {
        return entity.hasItemInInventory(itemName);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitHasItemCommand(this);
    }

    public String getName() {
        return itemName;
    }
}
