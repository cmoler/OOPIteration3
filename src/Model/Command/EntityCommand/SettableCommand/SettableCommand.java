package Model.Command.EntityCommand.SettableCommand;

import Model.Command.Command;

public interface SettableCommand extends Command {

    int getAmount();

    void setAmount(int amount);
}