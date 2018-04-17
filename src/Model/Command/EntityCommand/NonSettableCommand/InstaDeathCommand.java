package Model.Command.EntityCommand.NonSettableCommand;

import Model.Command.Command;
import Model.Entity.Entity;

public class InstaDeathCommand implements Command {

    public void execute(Entity entity) {
        entity.kill();
    }
}
