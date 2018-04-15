package Model.Command.LevelCommand;

import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public class PickPocketCommand extends LevelCommand {

    public PickPocketCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {

    }

    @Override
    public void execute(Entity entity) {

    }

}
