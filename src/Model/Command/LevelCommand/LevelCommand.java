package Model.Command.LevelCommand;

import Model.Command.Command;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public abstract class LevelCommand implements Command {

    private LevelMessenger levelMessenger;

    public LevelCommand(LevelMessenger levelMessenger) {
        this.levelMessenger = levelMessenger;
    }

    protected void sendSelfToLevel() {
        levelMessenger.sendCommandToLevel(this);
    }

    public abstract void recieveLevel(Level level);
}