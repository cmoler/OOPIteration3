package Model.Item;

import Model.Command.Command;
import Model.Entity.Entity;

public abstract class Item {

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

    protected Command getCommand() {
        return command;
    }
}
