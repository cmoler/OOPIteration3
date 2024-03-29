package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.SlowedAI;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class SlowEntityCommand extends GameModelCommand implements SettableCommand {

    private Entity entity;

    private int slowDuration;

    public SlowEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
        this.slowDuration = 100;
    }

    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(entity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new SlowedAI(previousState.getEntity(),aiController, previousState, slowDuration));
    }

    public void receiveLevel(Level level) {}

    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel();
    }

    public void setAmount(int amount) {
        this.slowDuration = slowDuration * amount;
    }

    public int getAmount() {
        return slowDuration;
    }

    public void accept(Visitor visitor) {
        visitor.visitSlowEntityCommand(this);
    }

    public Entity getEntity() {
        return entity;
    }
}
