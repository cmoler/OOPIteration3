package Model.Command.EntityCommand.ToggleableCommand;

import Model.Entity.Entity;

public class ToggleManaCommand extends ToggleableCommand {

    public ToggleManaCommand(int amount, boolean hasFired) {
        super(amount, hasFired);
    }

    @Override
    public void execute(Entity entity) {
        if(!hasFired){
            entity.increaseMana(amount);
            hasFired = !hasFired;
        }else{
            entity.decreaseMana(amount);
            hasFired = !hasFired;
        }
    }
}
