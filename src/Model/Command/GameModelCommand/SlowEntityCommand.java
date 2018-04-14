package Model.Command.GameModelCommand;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.SlowedAI;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.LevelMessenger;

public class SlowEntityCommand extends GameModelCommand {

    private Entity entity;

    public SlowEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(entity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new SlowedAI(aiController, previousState));
    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel(this);
    }
}
