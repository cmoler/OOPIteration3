package Model.AreaEffect;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.AreaEffectView;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

public class InfiniteAreaEffect implements AreaEffect {

    private Command command;
    private AreaEffectView observer;

    public InfiniteAreaEffect(Command command) {
        this.command = command;
    }

    public void trigger(Entity entity) {
        command.execute(entity);
    }

    public Command getCommand() {
        return command;
    }

    public void accept(Visitor visitor) {
        visitor.visitInfiniteAreaEffect(this);
    }

    public void setObserver(AreaEffectView observer) {
        this.observer = observer;
    }

    public AreaEffectView getObserver() {
        return observer;
    }

    public void notifyObserver(Point3D location) {
        if(observer == null) { return; }
        observer.setPosition(location);
    }
}
