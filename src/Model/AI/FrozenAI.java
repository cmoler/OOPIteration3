package Model.AI;

import Controller.Visitor.SavingVisitor;
import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import javafx.geometry.Point3D;

import java.util.Map;

public class FrozenAI extends AIState {

    private long startTime;
    private long duration;
    private AIController controller;
    private AIState previousState;
    private int currentTick = 0;

    public FrozenAI(Entity entity, AIController controller, AIState aiState, long duration){
        super(entity);
        startTime = System.currentTimeMillis();
        this.controller = controller;
        previousState = aiState;
        this.duration = duration;
        System.out.println("FrozenAI: " + duration);

    }


    @Override
    public void nextMove() {
        if(currentTick > duration) {
            System.out.println("Reseting");
            controller.setActiveState(previousState);
        }
        else if(currentTick % 20 ==0 ){
            //previousState.nextMove();
        }
        currentTick++;
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitFrozenAI(this);
    }
}
