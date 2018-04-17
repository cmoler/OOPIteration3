package Model.Command.EntityCommand;

import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;

public class AddHealthCommand implements SettableCommand {

    int healAmount;

    public AddHealthCommand(int healAmount) {
        this.healAmount = healAmount;
    }

    public void execute(Entity entity) {
        entity.increaseHealth(healAmount);
    }

    public void setAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public int getAmount() {
        return healAmount;
    }
}
