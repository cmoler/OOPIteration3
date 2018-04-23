package Model.Item;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Condition.Condition;
import Model.Entity.Entity;

public class InteractiveItem extends Item {

    private Condition condition;

    public InteractiveItem(String name, Command command) {
        super(name, command);
    }
    public InteractiveItem(String name, Command command, Condition condition) {
        super(name, command);
        this.condition = condition;
    }

    public void onTouch(Entity entity) {
        if(condition != null) {
            if (condition.checkCondition(entity)) {
                executeCommand(entity);
            }
        }

        else {
            executeCommand(entity);
        }
    }

    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }

    public Condition getCondition() {
        return condition;
    }
}
