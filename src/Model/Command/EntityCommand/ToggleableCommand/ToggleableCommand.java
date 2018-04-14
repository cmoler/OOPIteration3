package Model.Command.EntityCommand.ToggleableCommand;

import Model.Command.Command;

public abstract class ToggleableCommand implements Command {

    protected int amount;
    protected boolean hasFired;

    public void setAmount(int amount){
        this.amount = amount;
    }
}
