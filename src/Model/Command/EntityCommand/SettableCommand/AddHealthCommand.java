package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.Entity.Entity;

public class AddHealthCommand implements SettableCommand {

    int healAmount;

    public AddHealthCommand(int healAmount) {
        this.healAmount = healAmount;
    }

    public void execute(Entity entity) {
        entity.increaseHealth(healAmount);
    }

    public void setAmount(int healAmount) {
        this.healAmount = healAmount;
    }

    public int getAmount() {
        return healAmount;
    }

    public void accept(Visitor visitor) {
        visitor.visitAddHealthCommand(this);
    }
}
