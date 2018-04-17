package Model.Command.EntityCommand.NonSettableCommand;

import Controller.GameLoop;
import Model.Command.Command;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class DialogCommand extends GameLoopCommand implements Command {

    private Entity entity;

    public DialogCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameLoop(GameLoop gameLoop) {
        gameLoop.openDialogWindow(entity);
    }

    @Override
    public void receiveLevel(Level level) {

    }

    @Override
    public void receiveGameModel(GameModel gameModel) {

    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameLoop();
    }
}
