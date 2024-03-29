package Model.Level;

import Controller.GameLoop;
import Model.Command.GameLoopCommand.GameLoopCommand;

public class GameLoopMessenger {

    private GameLoop gameLoop;

    public GameLoopMessenger(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public void sendCommandToGameLoop(GameLoopCommand command) {
        command.receiveGameLoop(gameLoop);
    }
}
