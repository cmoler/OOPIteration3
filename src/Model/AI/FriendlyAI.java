package Model.AI;

import Model.Entity.Entity;
import Model.Entity.EntityAttributes.VectorToPointCalculator;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.Map;
import java.util.Random;

public class FriendlyAI extends AIState{
    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Entity> entityMap;
    private Map<Point3D, Obstacle> obstacleMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;
    private PatrolPath patrolPath;
    private Point3D origin;
    private double moveRadius;
    private Boolean moveToOrigin;

    public FriendlyAI(Entity ent, Map<Point3D, Terrain> terrainMap, Map<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player) {
        super(ent);
        this.terrainMap = terrainMap;
        this.entityMap = entityMap;
        this.obstacleMap = obstacleMap;
        this.player = player;
        this.pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        origin = getEntityPoint(super.getEntity(), entityMap);
        moveRadius = getEntity().getSight();
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        if (getEntity().isMoveable()) {
            boolean calcNeeded;
            Point3D destination;
            Vec3d randVelocity = super.generateRandomVelcity();
            do {
                destination = VectorToPointCalculator.calculateNewPoint(position, randVelocity);
                if(destination.distance(origin) >= moveRadius){
                    calcNeeded = true;
                    randVelocity = super.generateRandomVelcity();
                }
                else{
                    calcNeeded = false;
                }
            } while(calcNeeded);

            super.getEntity().addVelocity(randVelocity);
        }
    }

    private Point3D getEntityPoint(Entity entity, Map<Point3D, Entity> entityLocations) {
        if(entityLocations.containsValue(entity)) {
            for(Point3D point: entityLocations.keySet()) {
                if(entityLocations.get(point) == entity) {
                    return point;
                }
            }
        }
        return new Point3D(0,0,0);
    }

}
