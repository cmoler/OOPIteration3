package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class LevelUpCommand implements Command {

    public void execute(Entity entity) {
        entity.levelUp();
    }

    @Override
    public void accept(SavingVisitor savingVisitor) {
        savingVisitor.visitLevelUpCommand(this);
    }
}
