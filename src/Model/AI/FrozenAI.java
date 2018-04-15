package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import javafx.geometry.Point3D;

import java.util.Map;

public class FrozenAI extends AIState {

    private long startTime;
    private long duration = 7000;
    private AIController controller;
    private AIState previousState;

    public FrozenAI(Entity entity, AIController controller, AIState aiState){
        super(entity);
        startTime = System.currentTimeMillis();
        this.controller = controller;
        previousState = aiState;
    }


    @Override
    public void nextMove() {

    }
}
