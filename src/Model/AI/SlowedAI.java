package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import javafx.geometry.Point3D;

import java.util.Map;

public class SlowedAI extends AIState {

    private AIState aiState;
    private AIController aiController;
    private int delay = 120;
    private int currentTick = 0;

    public SlowedAI(Entity entity, AIController aiController, AIState aiState){
        super(entity);
        this.aiController = aiController;
        this.aiState = aiState;
    }


    @Override
    public void nextMove() {
        if(currentTick > delay) aiController.setActiveState(aiState);
        else if(currentTick % 20 ==0 ){
            aiState.nextMove();
        }
        currentTick++;
    }
}
