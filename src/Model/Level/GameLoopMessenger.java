package Model.Level;

import Controller.GameLoop;
import Model.Command.GameLoopCommand.GameLoopCommand;

public class GameLoopMessenger {

    private GameLoop gameLoop;

    public void sendCommandToGameLoop(GameLoopCommand command) {
        gameLoop.receiveGameLoopCommand(command);
    }
}
