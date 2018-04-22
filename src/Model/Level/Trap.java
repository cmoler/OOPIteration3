package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;
import View.LevelView.LevelViewElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Trap implements Visitable {

    private LevelViewElement observer;

    private boolean isVisible;
    private boolean isDisarmed;

    private Command command;

    private int trapStrength;

    public Trap(Command command, boolean isVisible, boolean isDisarmed, int trapStrength) {

        this.isVisible = isVisible;
        this.isDisarmed = isDisarmed;
        this.command = command;
        this.trapStrength = trapStrength;
    }

    public Trap(Command command, int trapStrength) {

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

            if(observer != null)
                observer.notifyViewElement();

        }
    }


    public void disarm(Entity entity, int disarmStrength) {
        if(trapStrength < disarmStrength) {
            if (!isVisible) {
                isVisible = true;
            } else if (!isDisarmed) {
                isDisarmed = true;
            }

            if(observer != null)
                observer.notifyViewElement();

        } else {
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

    public void accept(Visitor visitor) {
        visitor.visitTrap(this);
    }

    public void setObserver(LevelViewElement observer) {
        this.observer = observer;
    }
}
