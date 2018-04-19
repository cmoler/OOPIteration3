package Model.Level;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.LevelViewElement;

import java.util.List;
import java.util.Random;

public class Trap implements Visitable {

    private List<LevelViewElement> observers;

    private boolean isVisible;
    private boolean isDisarmed;

    private Command command;

    private int trapStrength;

    public Trap(List<LevelViewElement> observers, Command command, boolean isVisible, boolean isDisarmed, int trapStrength) {
        this.observers = observers;
        this.isVisible = isVisible;
        this.isDisarmed = isDisarmed;
        this.command = command;
        this.trapStrength = trapStrength;
    }

    public Trap(List<LevelViewElement> observers, Command command, int trapStrength) {
        this.observers = observers;
        this.trapStrength = trapStrength;

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


    public void disarm(Entity entity, int disarmStrength) {
        if(trapStrength < disarmStrength) {
            if (!isVisible) {
                isVisible = true;
            } else if (!isDisarmed) {
                isDisarmed = true;
            }

            for (LevelViewElement observer : observers) {
                observer.notifyViewElement();
            }
        } else { // TODO: make failure state for trap disarming more complex (right now it just triggers the trap on the entity if it fails the disarm)
            this.fire(entity);
        }
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public boolean getIsDisarmed() {
        return isDisarmed;
    }

    public int getTrapStrength() { return trapStrength; }

    public Command getCommand() { return command; }

    public void accept(SavingVisitor visitor) {
        visitor.visitTrap(this);
    }
}
