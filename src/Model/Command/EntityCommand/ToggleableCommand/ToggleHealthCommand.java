package Model.Command.EntityCommand.ToggleableCommand;

import Model.Entity.Entity;

public class ToggleHealthCommand extends ToggleableCommand {

    @Override
    public void execute(Entity entity) {
        if(!hasFired){
            entity.increaseHealth(amount);
            hasFired = !hasFired;
        }else{
            entity.decreaseHealth(amount);
            hasFired = !hasFired;
        }
    }
}
