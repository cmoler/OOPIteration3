package Model.Command.LevelCommand;

import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class DisarmTrapCommand extends LevelCommand {

    private Entity entity;

    public DisarmTrapCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void recieveLevel(Level level) {
        level.disarmTrapFromEntity(entity);
    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendSelfToLevel();
    }
}
