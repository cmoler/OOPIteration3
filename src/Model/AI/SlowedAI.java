package Model.AI;

import Model.Level.Level;

public class SlowedAI extends AIState {

    private AIState aiState;
    private AIController aiController;
    private int delay = 120;
    private int currentTick = 0;

    public SlowedAI(AIController aiController, AIState aiState){
        this.aiController = aiController;
        this.aiState = aiState;
    }

    @Override
    public void nextMove(Level level) {
        if(currentTick > delay) aiController.setActiveState(aiState);
        else if(currentTick % 20 ==0 ){
            aiState.nextMove(level);
        }
        currentTick++;
    }
}
