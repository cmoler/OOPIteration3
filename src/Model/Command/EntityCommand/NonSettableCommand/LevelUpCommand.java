package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class LevelUpCommand implements Command {

    public void execute(Entity entity) {
        entity.levelUp();
    }

    public void accept(Visitor visitor) {
        visitor.visitLevelUpCommand(this);
    }
}
