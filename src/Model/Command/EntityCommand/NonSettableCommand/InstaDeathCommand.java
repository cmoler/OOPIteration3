package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class InstaDeathCommand implements Command {

    public void execute(Entity entity) {
        entity.kill();
    }

    public void accept(Visitor visitor) {
        visitor.visitInstaDeathCommand(this);
    }
}
