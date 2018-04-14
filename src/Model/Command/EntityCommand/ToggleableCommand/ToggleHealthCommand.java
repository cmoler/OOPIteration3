package Model.Command.EntityCommand.ToggleableCommand;

import Model.Entity.Entity;

public class ToggleHealthCommand extends ToggleableCommand {

    public ToggleHealthCommand(int amount) {
        setAmount(amount);
    }

    @Override
    public void execute(Entity entity) {
        if(!hasFired){
            entity.increaseMaxHealth(amount);
            hasFired = !hasFired;
        }else{
            entity.decreaseMaxHealth(amount);
            hasFired = !hasFired;
        }
    }
}
