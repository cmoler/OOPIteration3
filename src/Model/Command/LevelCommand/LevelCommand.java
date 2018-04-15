package Model.Command.LevelCommand;

import Model.Command.Command;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public abstract class LevelCommand implements Command {

    private LevelMessenger levelMessenger;

    public LevelCommand(LevelMessenger levelMessenger) {
        this.levelMessenger = levelMessenger;
    }

    public void sendCommandToLevel(LevelCommand levelCommand) {
        levelMessenger.sendLevelCommandToGameModel(levelCommand);
    }

    public abstract void receiveGameModel(GameModel gameModel);
}
