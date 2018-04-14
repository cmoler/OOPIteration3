package Model.Level;

import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.LevelViewElement;

import java.util.List;

public class Trap {

    private List<LevelViewElement> observers;

    private boolean isVisible;
    private boolean isDisarmed;

    private Command command;

    public Trap(List<LevelViewElement> observers, Command command) {
        this.observers = observers;

        isVisible = false;
        isDisarmed = false;

        this.command = command;
    }

    public void fire(Entity entity) {
        if(!isDisarmed) {
            command.execute(entity);
            isDisarmed = true;
            isVisible = true;

            for(LevelViewElement observer : observers) {
                observer.notifyViewElement();
            }
        }
    }

    // TODO: define disarm method logic -> wait until skill for it is created
    public void disarm() {
        // TODO notify observers when trap is disarmed
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public boolean getIsDisarmed() {
        return isDisarmed;
    }
}
