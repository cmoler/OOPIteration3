package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;

public class FrozenAI extends AIState {

    private long startTime;
    private long duration = 7000;
    private AIController controller;
    private AIState previousState;

    public FrozenAI(AIController controller, AIState aiState){
        startTime = System.currentTimeMillis();
        this.controller = controller;
        previousState = aiState;
    }

    @Override
    public void nextMove(Level level) {
        if(System.currentTimeMillis() > startTime + duration){
            controller.setActiveState(previousState);
        }
    }
}
