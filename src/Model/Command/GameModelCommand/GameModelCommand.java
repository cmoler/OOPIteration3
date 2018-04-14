package Model.Command.GameModelCommand;

import Model.Command.Command;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public abstract class GameModelCommand implements Command {

    protected LevelMessenger levelMessenger;

    public GameModelCommand(LevelMessenger levelMessenger){
        this.levelMessenger = levelMessenger;
    }

    public void sendCommandToGameModel(GameModelCommand gameModelCommand){
        levelMessenger.sendCommandToGameModel(gameModelCommand);
    }

    public abstract void receiveGameModel(GameModel gameModel);
}
