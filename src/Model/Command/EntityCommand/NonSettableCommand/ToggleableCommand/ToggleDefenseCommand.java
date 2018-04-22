package Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand;

import Controller.Visitor.Visitor;
import Model.Entity.Entity;

public class ToggleDefenseCommand extends ToggleableCommand {

    private int amount;

    public ToggleDefenseCommand(int amount) {
        super();
        this.amount = amount;
    }

    public ToggleDefenseCommand(int amount, boolean hasFired) {
        super(hasFired);
        this.amount = amount;
    }
    @Override
    public void execute(Entity entity) {
        if(!hasFired()) {
            entity.increaseDefense(amount);
            toggleHasFired();
        } else {
            entity.decreaseDefense(amount);
            toggleHasFired();
        }
    }

    @Override
    public void accept(Visitor visitor) {

    }
}
