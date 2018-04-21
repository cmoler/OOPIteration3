package Model.AreaEffect;

import Controller.Visitor.Visitor;
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

    public Command getCommand() {
        return command;
    }

    public void accept(Visitor visitor) {
        visitor.visitInfiniteAreaEffect(this);
    }
}
