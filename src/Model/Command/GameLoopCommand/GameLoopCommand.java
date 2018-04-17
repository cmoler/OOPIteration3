package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Command.Command;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public abstract class GameLoopCommand {

    private LevelMessenger levelMessenger;

    public GameLoopCommand(LevelMessenger levelMessenger){
        this.levelMessenger = levelMessenger;
    }

    public void sendCommandToGameLoop() {
        levelMessenger.sendCommandToGameLoop(this);
    }

    public abstract void receiveGameLoop(GameLoop gameLoop);

    public abstract void receiveGameModel(GameModel gameModel);

    public abstract void receiveLevel(Level level);
}
