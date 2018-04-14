package Model.Command.EntityCommand.SettableEntityCommand;

import Model.Entity.Entity;

public class RemoveHealthCommand extends SettableCommand {

    public RemoveHealthCommand(int amount) {
        super(amount);
    }

    public void execute(Entity entity) {
        entity.decreaseHealth(getAmount());
    }
}
