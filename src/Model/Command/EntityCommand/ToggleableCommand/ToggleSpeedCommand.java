package Model.Command.EntityCommand.ToggleableCommand;

import Model.Entity.Entity;

public class ToggleSpeedCommand extends ToggleableCommand {
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
