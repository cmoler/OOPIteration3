package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
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

    @Override
    public void execute(Entity entity) {
        this.player = entity;
        sendCommandToGameLoop(this);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
