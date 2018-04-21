package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.ConfusedAI;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class ConfuseEntityCommand extends GameModelCommand implements SettableCommand {

    private Entity confusedEntity;
    private int confusionDuration;

    public ConfuseEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
        confusionDuration = 1000;
    }

    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(confusedEntity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new ConfusedAI(previousState.getEntity(), aiController, previousState, confusionDuration));
    }

    public void receiveLevel(Level level) {}

    public void execute(Entity entity) {
        this.confusedEntity = entity;
        sendCommandToGameModel();
    }

    public void setAmount(int amount) {
        this.confusionDuration = confusionDuration * amount;
    }

    public int getAmount() {
        return confusionDuration;
    }

    public Entity getEntity() {
        return confusedEntity;
    }

    public void accept(Visitor visitor) {
        visitor.visitConfuseEntityCommand(this);
    }
}
