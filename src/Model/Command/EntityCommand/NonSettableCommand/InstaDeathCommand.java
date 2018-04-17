package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class InstaDeathCommand implements Command {

    public void execute(Entity entity) {
        entity.kill();
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitInstaDeathCommand(this);
    }
}
