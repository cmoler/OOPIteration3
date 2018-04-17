package Model.Command.LevelCommand;

import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class DisarmTrapCommand extends LevelCommand implements SettableCommand {

    private Entity entity;

    int disarmStrength;

    public DisarmTrapCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
        this.disarmStrength = 0;
    }

    public void recieveLevel(Level level) {
        level.disarmTrapFromEntity(entity, disarmStrength);
    }

    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToLevel();
    }

    public void setAmount(int amount) {
        this.disarmStrength = amount;
    }

    public int getAmount() {
        return disarmStrength;
    }
}
