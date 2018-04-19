package Model.AI;

import Model.Entity.Entity;
import Model.Utility.BidiMap;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.RandomVelocityGenerator;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.*;


public class HostileAI extends AIState{
    private BidiMap<Point3D, Entity> entityMap;
    private List<Entity> targetList;
    private Entity player;
    private PathingAlgorithm pathCalculator;
    private PatrolPath patrolPath;
    private Point3D origin;
    private double chaseRadius;
    private Boolean moveToOrigin;

    public HostileAI(Entity ent, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, List<Entity> targetList) {
        super(ent);
        this.entityMap = entityMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        origin = getEntityPoint(super.getEntity(), entityMap);
        chaseRadius = getEntity().getSight();
        moveToOrigin = false;
        this.targetList = targetList;
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        Point3D goal = getTarget(position);

        if (isOutsideChaseRadius(position)){
            setReturnToOrigin();
        }

        if (isOutsideSightRange(position,goal)){
            moveAlongPatrol();
        }
        else if (isMovingToOrigin()){
            moveToGoal(position,origin);
        }
        else {
            moveToGoal(position, goal);
        }
    }

    private Point3D getTarget(Point3D position){
        Point3D playerPoint = getEntityPoint(player,entityMap);
        Point3D targetPoint = getNearestTarget(position);

        if (isSamePoint(playerPoint,targetPoint)){
            return playerPoint;
        }
        else if (isEquidistant(position,playerPoint,targetPoint)){
            return playerPoint; //Player takes highest priority
        }
        else{
            return getClosestPoint(origin,playerPoint,targetPoint);
        }
    }

    private boolean isSamePoint(Point3D point1, Point3D point2){
        return point1.equals(point2);
    }

    private Point3D getClosestPoint(Point3D origin, Point3D playerPoint, Point3D targetPoint) {
        double playerDistance = origin.distance(playerPoint);
        double targetDistance = origin.distance(targetPoint);
        if (playerDistance > targetDistance) {
            return targetPoint;
        }
        else{
            return playerPoint;
        }
    }

    private boolean isEquidistant(Point3D origin, Point3D point1, Point3D point2){
        return (origin.distance(point1) == origin.distance(point2));
    }

    private boolean isOutsideChaseRadius(Point3D position){
        return position.distance(origin) > chaseRadius;
    }

    private void setReturnToOrigin(){
        moveToOrigin = true;
    }

    private boolean isMovingToOrigin(){
        return moveToOrigin;
    }

    private boolean isOutsideSightRange(Point3D position, Point3D goal){
        return position.distance(goal) >= player.getNoise() && !moveToOrigin;
    }

    private void moveAlongPatrol() {
        if (patrolPath != null){
            super.getEntity().addVelocity(patrolPath.getNextMove());
        }
        else {
            super.getEntity().addVelocity(RandomVelocityGenerator.generateRandomVelocity());
        }
    }

    private void moveToGoal(Point3D start, Point3D goal){
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, player);
        Point3D firstStep = path.get(0);
        super.getEntity().addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getEntityPoint(Entity entity, BidiMap<Point3D, Entity> entityLocations) {
       return entityLocations.getKeyFromValue(entity);
    }

    public PatrolPath getPatrolPath() {
        return patrolPath;
    }

    public void setPatrolPath(PatrolPath patrolPath) {
        this.patrolPath = patrolPath;
    }

    private Point3D getNearestTarget(Point3D origin) {
        List<Point3D> targetPoints = getTargetPoints();
        Point3D minLocation = targetPoints.get(0);
        double minDistance = Double.MAX_VALUE;
        double distance;
        for (Point3D point : targetPoints) {
            if (!entityMap.getValueFromKey(point).equals(player)) { //TODO: Possible LoD violation?
                distance = origin.distance(point);
                if (distance < minDistance) {
                    minDistance = distance;
                    minLocation = point;
                }
            }
        }
        return minLocation;
    }

    private List<Point3D> getTargetPoints() {
        List<Point3D> targetPoints = new ArrayList<>();
        for (Entity ent: targetList) {
            targetPoints.add(entityMap.getKeyFromValue(ent));
        }
        return targetPoints;
    }

    public void addTarget(Entity ent){
        targetList.add(ent);
    }

    public void removeTarget(Entity ent){
        targetList.remove(ent);
    }
}
