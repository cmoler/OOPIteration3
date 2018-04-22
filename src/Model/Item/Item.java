package Model.Item;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.ItemView;
import javafx.geometry.Point3D;

public abstract class Item implements Visitable {

    private boolean toBeDeleted;
    private String name;
    private Command command;
    protected ItemView observer;

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

    public void clearDeletionFlag() {
        toBeDeleted = false;
    }

    protected void executeCommand(Entity entity) {
        command.execute(entity);
    }

    public Command getCommand() {
        return command;
    }

    public void setObserver(ItemView observer) {
        this.observer = observer;
    }

    public ItemView getObserver() {
        return observer;
    }

    public void notifyObserver(Point3D location) {
        if(observer == null) { return; }
        observer.setPosition(location);
    }
}
