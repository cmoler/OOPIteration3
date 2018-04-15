package Model.Level;

import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Command.LevelCommand.LevelCommand;
import Model.Command.LevelCommand.SendInfluenceEffectCommand;

public class LevelMessenger {

    private GameModelMessenger gameModelMessenger;
    private Level level;

    public LevelMessenger(GameModelMessenger gameModelMessenger, Level level) {
        this.gameModelMessenger = gameModelMessenger;
        this.level = level;
    }

    public void sendCommandToGameLoop(GameLoopCommand command) {
        gameModelMessenger.sendCommandToGameLoop(command);
    }

    public void sendCommandToGameModel(GameModelCommand command) {
        gameModelMessenger.sendCommandToGameModel(command);
    }

    public void sendLevelCommandToGameModel(LevelCommand levelCommand) {
        gameModelMessenger.sendLevelCommandToGameModel(levelCommand);
    }
}
