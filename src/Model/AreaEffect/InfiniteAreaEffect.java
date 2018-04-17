package Model.AreaEffect;

import Controller.Visitor.SavingVisitor;
import Model.Command.Command;
import Model.Entity.Entity;

public class InfiniteAreaEffect implements AreaEffect {

    private Command command;

    public InfiniteAreaEffect(Command command) {
        this.command = command;
    }

    public void trigger(Entity entity) {
        command.execute(entity);
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitInfiniteAreaEffect(this);
    }

    public Command getCommand() {
        return command;
    }
}
