package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Level.LevelMessenger;

public class ObserveEntityCommand extends GameLoopCommand implements SettableCommand {

    private Entity entity;

    int amount; // TODO: implement me pls thnx

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

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
