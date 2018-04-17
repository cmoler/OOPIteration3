package Model.AI;

import Model.Entity.Entity;

import java.util.Random;

public class ConfusedAI extends AIState{

    private long startTime;
    private long duration;
    private AIController controller;
    private AIState previousState;


    public ConfusedAI(Entity entity, AIController controller, AIState aiState, long duration){
        super(entity);
        startTime = System.currentTimeMillis();
        this.controller = controller;
        previousState = aiState;
        this.duration = duration;
    }

    @Override
    public void nextMove() {
        if(System.currentTimeMillis() > startTime + duration){
            controller.setActiveState(previousState);
        }
        else {
            Random rand = new Random();
            super.getEntity().addVelocity(super.generateRandomVelcity());
        }
    }
}
