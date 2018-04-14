package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Level.LevelMessenger;

public class DialogCommand extends GameLoopCommand {

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
