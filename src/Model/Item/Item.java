package Model.Item;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Entity.Entity;

public abstract class Item implements Visitable {

    private boolean toBeDeleted;
    private String name;
    private Command command;

    protected Item(String name, Command command) {
        this.name = name;
        this.command = command;
        toBeDeleted = false;
    }

    public abstract void onTouch(Entity entity);

    public boolean isToBeDeleted() {
        return toBeDeleted;
    }

    public String getName() {
        return name;
    }

    protected void setToBeDeleted() {
        toBeDeleted = true;
    }

    protected void executeCommand(Entity entity) {
        command.execute(entity);
    }

    public Command getCommand() {
        return command;
    }
}
