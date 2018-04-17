package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Command.Command;
import Model.Entity.Entity;
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
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameLoop(this);
    }
}
