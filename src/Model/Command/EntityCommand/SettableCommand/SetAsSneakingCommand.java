package Model.Command.EntityCommand.SettableCommand;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;

public class SetAsSneakingCommand implements SettableCommand {

    private int decreaseSpeedAmount = 5;
    private int decreaseNoiseAmount = 5;

    int amount; // TODO: implement me pls thnx you

    @Override
    public void execute(Entity entity) {
        entity.decreaseSpeed(decreaseSpeedAmount);
        entity.decreaseNoiseLevel(decreaseNoiseAmount);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
