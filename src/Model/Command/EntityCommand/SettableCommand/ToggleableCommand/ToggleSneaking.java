package Model.Command.EntityCommand.SettableCommand.ToggleableCommand;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;

public class ToggleSneaking extends ToggleableCommand implements SettableCommand {

    private int stealthAmount;
    private long oldSpeed;
    private int oldNoise;
    private boolean firstTimeExecuting;

    public ToggleSneaking(boolean hasFired, int stealthAmount, int oldSpeed, int oldNoise, boolean firstTimeExecuting) {
        super(hasFired);
        this.stealthAmount = stealthAmount;
        this.oldSpeed = oldSpeed;
        this.oldNoise = oldNoise;
        this.firstTimeExecuting = firstTimeExecuting;
    }

    public ToggleSneaking(int amount) {
        super();
        this.stealthAmount = amount;
        this.firstTimeExecuting = true;
    }

    public ToggleSneaking(int amount, boolean hasFired) {
        super(hasFired);
        this.stealthAmount = amount;
        this.firstTimeExecuting = true;
    }

    @Override
    public void execute(Entity entity) {
        if(firstTimeExecuting) {
            oldSpeed = entity.getSpeed();
            oldNoise = entity.getNoise();

            firstTimeExecuting = false;
        }

        if(!hasFired()) {
            entity.setSpeed(oldSpeed + oldSpeed);
            entity.decreaseNoiseLevel(stealthAmount);
            toggleHasFired();
        } else {
            entity.setSpeed(oldSpeed);
            entity.setNoise(oldNoise);
            toggleHasFired();
        }
    }

    public void setAmount(int amount) {
        this.stealthAmount = amount;
    }

    public int getAmount() {
        return stealthAmount;
    }

    public void accept(Visitor visitor) {
        visitor.visitToggleSneaking(this);
    }

    public long getOldSpeed() {
        return oldSpeed;
    }

    public int getOldNoise() {
        return oldNoise;
    }

    public boolean hasFirstTimeExecuted() {
        return firstTimeExecuting;
    }
}
