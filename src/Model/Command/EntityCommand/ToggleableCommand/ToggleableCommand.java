package Model.Command.EntityCommand.ToggleableCommand;

import Model.Command.Command;

public abstract class ToggleableCommand implements Command {

    protected int amount;
    protected boolean hasFired;

    public ToggleableCommand() {}

    public ToggleableCommand(int amount, boolean hasFired) {
        this.amount = amount;
        this.hasFired = hasFired;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }
}
