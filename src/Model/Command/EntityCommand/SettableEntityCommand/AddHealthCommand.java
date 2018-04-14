package Model.Command.EntityCommand.SettableEntityCommand;

import Model.Entity.Entity;

public class AddHealthCommand extends SettableCommand {

    public AddHealthCommand(int amount) {
        super(amount);
    }

    public void execute(Entity entity) {
        entity.increaseHealth(getAmount());
    }
}
