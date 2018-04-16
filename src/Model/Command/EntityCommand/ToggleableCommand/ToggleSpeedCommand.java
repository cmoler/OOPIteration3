package Model.Command.EntityCommand.ToggleableCommand;

import Model.Entity.Entity;

public class ToggleSpeedCommand extends ToggleableCommand {

    public ToggleSpeedCommand(int amount, boolean hasFired) {
        super(amount, hasFired);
    }

    @Override
    public void execute(Entity entity) {
        if(!hasFired){
            entity.increaseSpeed(amount);
            hasFired = !hasFired;
        }else{
            entity.decreaseSpeed(amount);
            hasFired = !hasFired;
        }
    }
}
