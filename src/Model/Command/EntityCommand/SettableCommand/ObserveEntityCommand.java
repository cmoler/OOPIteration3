package Model.Command.EntityCommand.SettableCommand;

import Controller.GameLoop;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
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
    public void receiveGameModel(GameModel gameModel) {

    }

    @Override
    public void receiveLevel(Level level) {

    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameLoop();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
