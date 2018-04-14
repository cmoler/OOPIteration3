package Model.Command.EntityCommand;

import Model.Command.Command;
import Model.Entity.Entity;

public class SetAsSneakingCommand implements Command {

    private int decreaseSpeedAmount = 5;
    private int decreaseNoiseAmount = 5;

    @Override
    public void execute(Entity entity) {
        entity.decreaseSpeed(decreaseSpeedAmount);
        entity.decreaseNoiseLevel(decreaseNoiseAmount);
    }
}
