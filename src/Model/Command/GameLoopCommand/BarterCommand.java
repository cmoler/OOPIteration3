package Model.Command.GameLoopCommand;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Level.LevelMessenger;

public class BarterCommand extends GameLoopCommand {

    private Entity player;
    private Entity npcTradingWith;
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
}
