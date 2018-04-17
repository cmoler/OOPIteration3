package Model.Command.GameModelCommand;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.FrozenAI;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public class FreezeEntityCommand extends GameModelCommand implements SettableCommand {

    private Entity entity;

    // TODO: implement pls
    private int amount;

    public FreezeEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(entity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new FrozenAI(previousState.getEntity(), aiController, previousState));
    }


    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel(this);
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
