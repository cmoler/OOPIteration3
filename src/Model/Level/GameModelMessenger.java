package Model.Level;

import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Command.LevelCommand.LevelCommand;

public class GameModelMessenger {

    private GameModel gameModel;
    private GameLoopMessenger gameLoopMessenger;

    public GameModelMessenger(GameModel gameModel, GameLoopMessenger gameLoopMessenger) {
        this.gameModel = gameModel;
        this.gameLoopMessenger = gameLoopMessenger;
    }

    public void sendCommandToGameLoop(GameLoopCommand command) {
        command.receiveGameModel(gameModel);

        gameLoopMessenger.sendCommandToGameLoop(command);
    }

    public void sendCommandToGameModel(GameModelCommand command) {
        command.receiveGameModel(gameModel);
    }
}
