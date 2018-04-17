package Model.Item;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class InteractiveItem extends Item {

    public InteractiveItem(String name, Command command) {
        super(name, command);
    }

    public void onTouch(Entity entity) {
        executeCommand(entity);
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitItem(this);
    }
}
