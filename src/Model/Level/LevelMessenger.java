package Model.Level;

import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Command.LevelCommand.LevelCommand;

public class LevelMessenger {

    private GameModelMessenger gameModelMessenger;
    private Level level;

    public LevelMessenger(GameModelMessenger gameModelMessenger, Level level) {
        this.gameModelMessenger = gameModelMessenger;
        this.level = level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void sendCommandToGameLoop(GameLoopCommand command) {
        command.receiveLevel(level);

        gameModelMessenger.sendCommandToGameLoop(command);
    }

    public void sendCommandToGameModel(GameModelCommand command) {
        command.receiveLevel(level);

        gameModelMessenger.sendCommandToGameModel(command);
    }

    public void sendCommandToLevel(LevelCommand levelCommand) {
        levelCommand.receiveLevel(level);
    }
}
