package Model.Command.EntityCommand.SettableCommand;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.AI.ConfusedAI;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Item.TakeableItem.TakeableItem;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

import java.util.Random;

public class PickPocketCommand extends GameModelCommand implements SettableCommand {

    private Entity invokingEntity;

    private int pickPocketStrength;

    public PickPocketCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void receiveLevel(Level level) {
        Point3D invokerPoint = level.getEntityPoint(invokingEntity);

        Point3D destPoint = Orientation.getAdjacentPoint(invokerPoint, invokingEntity.getOrientation());

        Entity entityToStealFrom = level.getEntityAtPoint(destPoint);

        if(entityToStealFrom != null) {

            Random rand = new Random();

            int successChance = rand.nextInt(pickPocketStrength);

            if(successChance < pickPocketStrength / 2) { // successful pickpocket
                TakeableItem item = entityToStealFrom.takeRandomItemFromInventory();

                if(item != null) {
                    invokingEntity.addItemToInventory(item);
                }

            } else { // unsuccessful pickpocket, turn entity hostile
                switch (rand.nextInt(2)) { // flip a coin to see if entity should notice the failed theft
                    case 0:
                    //    AIController aiController = gameModel.getAIForEntity(entityToStealFrom);
                    //    AIState previousState = aiController.getActiveState();
                    //    aiController.setActiveState(new ConfusedAI(previousState.getEntity(), aiController, previousState));
                        break;
                    case 1:
                        break;
                    default:
                        throw new RuntimeException("Invalid random generated for PickPocketCommand!");
                }
            }
        }
    }

    public void receiveGameModel(GameModel gameModel) {

    }

    public void execute(Entity entity) {
        this.invokingEntity = entity;
        sendCommandToGameModel();
    }

    public void setAmount(int amount) {
        this.pickPocketStrength = amount;
    }

    public int getAmount() {
        return pickPocketStrength;
    }

}
