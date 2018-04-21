package Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand;

import Controller.Visitor.Visitor;
import Model.Entity.Entity;

public class ToggleManaCommand extends ToggleableCommand {

    int amount;

    public ToggleManaCommand(int amount) {
        super();
        this.amount = amount;
    }

    public ToggleManaCommand(int amount, boolean hasFired) {
        super(hasFired);
        this.amount = amount;
    }

    public void execute(Entity entity) {
        if(!hasFired()){
            entity.increaseMana(amount);
            toggleHasFired();
        } else{
            entity.decreaseMana(amount);
            toggleHasFired();
        }
    }

    public int getAmount() {
        return amount;
    }

    public void accept(Visitor visitor) {
        visitor.visitToggleManaCommand(this);
    }
}
