package Model.Command.EntityCommand.SettableCommand.ToggleableCommand;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleableCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;

public class ToggleSneaking extends ToggleableCommand implements SettableCommand {

    private int stealthAmount;
    private int oldSpeed;
    private int oldNoise;
    private boolean firstTimeExecuting;

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
            entity.decreaseSpeed(stealthAmount);
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
}
