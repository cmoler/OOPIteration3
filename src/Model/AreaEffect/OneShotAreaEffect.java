package Model.AreaEffect;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.AreaEffectView;
import View.LevelView.ItemView;
import javafx.geometry.Point3D;

public class OneShotAreaEffect implements AreaEffect {

    private Command command;
    private boolean hasNotFired;
    private AreaEffectView observer;

    public OneShotAreaEffect(Command command) {
        this.command = command;
        hasNotFired = true;
    }

    public void trigger(Entity entity) {
        if(hasNotFired) {
            command.execute(entity);
            hasNotFired = false;
        }
    }

    public Command getCommand() {
        return command;
    }

    public boolean hasNotFired() {
        return hasNotFired;
    }

    public void accept(Visitor visitor) {
        visitor.visitOneShotAreaEffect(this);
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
