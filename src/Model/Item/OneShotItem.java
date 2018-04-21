package Model.Item;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class OneShotItem extends Item {

    public OneShotItem(String name, Command command) {
        super(name, command);
    }

    public void onTouch(Entity entity) {
        executeCommand(entity);
        setToBeDeleted();
    }

    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }
}
