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
        Entity target = entityMap.getValueFromKey(goal);
        System.out.println("The target's point is:\t" + goal);

        /*if (!isReturningToOrigin() && isOutsideChaseRadius(position)){
            setReturnToOrigin();
            System.out.println("Outside chase radius!");
        }
        else if (isAtOrigin(position)){
            resetMoveToOrigin();
            //System.out.println("\n\nReturn reset\n");
        }*/

        if (isOutsideSightRange(target,position,goal) && hasPatrolSet()){
            moveAlongPatrol();
        }
        /*else if (isMovingToOrigin() && !isAtOrigin(position)){
            moveToGoal(position,origin);
            System.out.println("Moving to origin!");
        }*/
        else {
            moveToGoal(position, goal);
        }
    }

    private boolean hasPatrolSet() {
        return patrolPath != null;
    }

    private boolean isReturningToOrigin() {
        return moveToOrigin;
    }

    private boolean isAtOrigin(Point3D currentPosition) {
        return currentPosition.distance(origin) == 0;
    }

    private void resetMoveToOrigin(){
        moveToOrigin = false;
    }

    private Point3D getTarget(Point3D position){
        return getNearestTarget(position);
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

    private boolean isOutsideSightRange(Entity target, Point3D position, Point3D goal){
        return position.distance(goal) >= target.getNoise() && !moveToOrigin;
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
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, super.getEntity());
        Point3D firstStep = path.get(path.size()-1);
        System.out.println("My Point:\t"+start);
        //System.out.println("Is this my first step?: Let's see:\tCurrent Point:\t"+start+"\tFirst step:\t"+firstStep);
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
            distance = origin.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                minLocation = point;
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
