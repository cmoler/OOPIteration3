package Model.Level;

import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.LevelViewElement;

import java.util.List;
import java.util.Random;

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


    public void disarm() {
        // TODO: add logic for being able to fail a disarm based on skill level of entity that calls disarm trap skill

        if(!isVisible) {
            isVisible = true;
        } else if(!isDisarmed) {
            isDisarmed = true;
        }

        for(LevelViewElement observer : observers) {
            observer.notifyViewElement();
        }
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public boolean getIsDisarmed() {
        return isDisarmed;
    }
}
