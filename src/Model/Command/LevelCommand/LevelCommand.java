package Model.Command.LevelCommand;

import Model.Command.Command;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public abstract class LevelCommand {

    private LevelMessenger levelMessenger;

    public LevelCommand(LevelMessenger levelMessenger) {
        this.levelMessenger = levelMessenger;
    }

    protected void sendCommandToLevel() {
        levelMessenger.sendCommandToLevel(this);
    }

    public abstract void recieveLevel(Level level);
}