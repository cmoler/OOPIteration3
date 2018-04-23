package Model.AI;

import Model.Entity.Entity;
import Model.Level.River;
import Model.Utility.VectorToPointCalculator;
import Model.Utility.BidiMap;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.RandomVelocityGenerator;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.Map;

public class FriendlyAI extends AIState{
    private BidiMap<Point3D, Entity> entityMap;
    private PathingAlgorithm pathCalculator;
    private PatrolPath patrolPath;
    private Point3D origin;
    private double moveRadius;

    public FriendlyAI(Entity ent, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, River> riverMap) {
        super(ent);
        this.entityMap = entityMap;
        this.pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap, riverMap,entityMap );
        origin = getEntityPoint(super.getEntity(), entityMap);
        moveRadius = super.getEntity().getSight();
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        if (getEntity().isMoveable()) {
            if (hasPatrolPath()) {
                moveAlongPath();
            }
            else {
                moveRandomly(position);
            }
        }
    }

    private boolean hasPatrolPath(){
        return patrolPath != null;
    }

    private void moveAlongPath(){
        super.getEntity().addVelocityFromControllerInput(patrolPath.getNextMove());
    }

    private void moveRandomly(Point3D position){
        boolean calcNeeded;
        Point3D destination;
        Vec3d randVelocity = RandomVelocityGenerator.generateRandomVelocity();
        do {
            destination = VectorToPointCalculator.calculateNewPoint(position, randVelocity);
            if (destination.distance(origin) >= moveRadius) {
                calcNeeded = true;
                randVelocity = RandomVelocityGenerator.generateRandomVelocity();
            } else {
                calcNeeded = false;
            }
        } while (calcNeeded);

        super.getEntity().addVelocityFromControllerInput(randVelocity);
    }

    private Point3D getEntityPoint(Entity entity, BidiMap<Point3D, Entity> entityLocations) {
        return entityLocations.getKeyFromValue(entity);
    }

    public void setPatrolPath(PatrolPath patrolPath) {
        this.patrolPath = patrolPath;
    }
}
