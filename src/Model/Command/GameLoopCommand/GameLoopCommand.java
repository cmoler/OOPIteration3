package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Command.Command;
import Model.Level.LevelMessenger;

public abstract class GameLoopCommand implements Command {

    protected LevelMessenger levelMessenger;

    public GameLoopCommand(LevelMessenger levelMessenger){
        this.levelMessenger = levelMessenger;
    }

    public void sendCommandToGameLoop(GameLoopCommand gameLoopCommand){
        levelMessenger.sendCommandToGameLoop(gameLoopCommand);
    }

    public abstract void receiveGameLoop(GameLoop gameLoop);
}
