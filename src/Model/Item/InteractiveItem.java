package Model.Item;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class InteractiveItem extends Item {

    public InteractiveItem(String name, Command command) {
        super(name, command);
    }

    public void onTouch(Entity entity) {
        executeCommand(entity);
    }

    public void accept(Visitor visitor) {
        visitor.visitItem(this);
    }
}
