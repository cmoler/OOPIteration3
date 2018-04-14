package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Level.LevelMessenger;

public class ObserveEntityCommand extends GameLoopCommand {

    private Entity entity;

    public ObserveEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameLoop(GameLoop gameLoop) {
        gameLoop.createObservationWindow(entity);
    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameLoop(this);
    }
}
