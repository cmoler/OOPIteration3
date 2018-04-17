package Model.Command.EntityCommand.SettableCommand;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.FrozenAI;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class FreezeEntityCommand extends GameModelCommand implements SettableCommand {

    private Entity entity;

    private int freezeDuration;

    public FreezeEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
        this.freezeDuration = 1000;
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {
        AIController aiController = gameModel.getAIForEntity(entity);
        AIState previousState = aiController.getActiveState();
        aiController.setActiveState(new FrozenAI(previousState.getEntity(), aiController, previousState, freezeDuration));
    }

    public void receiveLevel(Level level) {

    }


    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel();
    }

    public void setAmount(int amount) {
        this.freezeDuration = freezeDuration * amount;
    }

    public int getAmount() {
        return freezeDuration;
    }
}
