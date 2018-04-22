package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.Entity.Entity;

public class AddManaCommand implements SettableCommand {

    int manaAmount;

    public AddManaCommand(int manaAmount) { this.manaAmount = manaAmount; }
    @Override
    public int getAmount() {
        return manaAmount;
    }

    @Override
    public void setAmount(int amount) {
        manaAmount = amount;
    }

    @Override
    public void execute(Entity entity) {
        entity.increaseMana(manaAmount);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitAddManaCommand(this);
    }
}
