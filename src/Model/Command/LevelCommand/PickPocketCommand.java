package Model.Command.LevelCommand;

import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class PickPocketCommand extends LevelCommand {

    public PickPocketCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void execute(Entity entity) {

    }

    @Override
    public void receiveLevel(Level level) {

    }
}
