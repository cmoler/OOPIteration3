//TODO: Have enemies also attack pets

package Model.AI;

import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.*;


public class HostileAI extends AIState{
    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Entity> entityMap;
    private Map<Point3D, Obstacle> obstacleMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;
    private PatrolPath patrolPath;
    private Point3D origin;
    private double chaseRadius;
    private Boolean moveToOrigin;

    public HostileAI(Entity ent, Map<Point3D, Terrain> terrainMap, Map<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player) {
        super(ent);
        this.terrainMap = terrainMap;
        this.entityMap = entityMap;
        this.obstacleMap = obstacleMap;
        this.player = player;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        origin = getEntityPoint(super.getEntity(), entityMap);
        chaseRadius = getEntity().getSight();
        moveToOrigin = false;
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        Point3D goal = getEntityPoint(player, entityMap);

        if (position.distance(origin) > chaseRadius){
            moveToOrigin = true;
        }

        if (position.distance(goal) >= player.getNoise() && !moveToOrigin){
            moveAlongPatrol();
        }
        else if (moveToOrigin){
            moveToGoal(position,origin);
        }
        else {
            moveToGoal(position, goal);
        }
    }

    private void moveAlongPatrol() {
        if (patrolPath != null){
            super.getEntity().addVelocity(patrolPath.getNextMove());
        }
        else {
            super.getEntity().addVelocity(super.generateRandomVelcity());
        }
    }

    private void moveToGoal(Point3D start, Point3D goal){
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, player);
        Point3D firstStep = path.get(0);
        super.getEntity().addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
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

    public PatrolPath getPatrolPath() {
        return patrolPath;
    }

    public void setPatrolPath(PatrolPath patrolPath) {
        this.patrolPath = patrolPath;
    }
}
