package Model.Command.GameModelCommand;

import Model.Command.Command;
import Model.Command.LevelCommand.LevelCommand;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public abstract class GameModelCommand  {

    private LevelMessenger levelMessenger;

    public GameModelCommand(LevelMessenger levelMessenger){
        this.levelMessenger = levelMessenger;
    }

    public void sendCommandToGameModel() {
        levelMessenger.sendCommandToGameModel(this);
    }

    public abstract void receiveGameModel(GameModel gameModel);

    public abstract void receiveLevel(Level level);
}
