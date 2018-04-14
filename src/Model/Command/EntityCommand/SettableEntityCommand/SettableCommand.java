package Model.Command.EntityCommand.SettableEntityCommand;

import Model.Command.Command;

public abstract class SettableCommand implements Command {

    private int amount;

    public SettableCommand(int amount) {
        this.amount = amount;
    }

    public void setAmount(int amt) {
        this.amount = amt;
    }

    protected int getAmount() {
        return amount;
    }
}
