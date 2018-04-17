package Model.Command.GameModelCommand;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.SlowedAI;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public class SlowEntityCommand extends GameModelCommand implements SettableCommand {

    // TODO: implement me pls thnx

    private Entity entity;

    private int amount;

    public SlowEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(entity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new SlowedAI(previousState.getEntity(),aiController, previousState));
    }

    @Override
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
