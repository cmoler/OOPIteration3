package Model.Command.LevelCommand;

import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class PickPocketCommand extends LevelCommand implements SettableCommand {

    // TODO: implement me pls
    private Entity entity;

    int amount;

    public PickPocketCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void recieveLevel(Level level) {

    }

    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToLevel();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

}
