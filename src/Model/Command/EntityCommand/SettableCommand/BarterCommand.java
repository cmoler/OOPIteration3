package Model.Command.EntityCommand.SettableCommand;

import Controller.GameLoop;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class BarterCommand extends GameLoopCommand implements SettableCommand {

    private Entity player;
    private Entity npcTradingWith;

    private int amount; // TODO: implement me pls

    public BarterCommand(LevelMessenger levelMessenger, Entity npcTradingWith) {
        super(levelMessenger);
        this.npcTradingWith = npcTradingWith;
    }

    @Override
    public void receiveGameLoop(GameLoop gameLoop) {
        gameLoop.openBarterWindow(player, npcTradingWith);
    }

    public void receiveGameModel(GameModel gameModel) {

    }

    public void receiveLevel(Level level) {

    }

    @Override
    public void execute(Entity entity) {
        this.player = entity;
        sendCommandToGameLoop();
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
