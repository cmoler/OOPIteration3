package Model.AI;

import Model.Entity.Entity;
import Model.Utility.BidiMap;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.HexDistanceCalculator;
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
    private Boolean originalState;

    public HostileAI(Entity ent, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap) {
        super(ent);
        this.entityMap = entityMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        origin = getEntityPoint(super.getEntity(), entityMap);
        chaseRadius = getEntity().getSight();
        this.targetList = ent.getTargetingList();
        originalState = true;
    }

    @Override
    public void nextMove() {
        Point3D position = getEntityPoint(super.getEntity(), entityMap);
        Point3D goal = getTarget(position);

        if(isInAttackRange(position,goal)){
            attack(position,goal);
        }
        else if(isReachable(position,goal,super.getEntity())){
            moveToGoal(position, goal);
            if (isInOriginalState()) {
                setOriginalState();
            }
        }
        else{
            if (!isInOriginalState()){
                getToOriginalState(position);
            }
            else {
                performDefaultAction();
            }
        }
    }

    private void attack(Point3D position, Point3D goal) {
        super.getEntity().setOrientation(PathingAlgorithm.calculateOrientation(position,goal));
        super.getEntity().attack();
    }

    private boolean isInAttackRange(Point3D position, Point3D goal) {
        return HexDistanceCalculator.getHexDistance(position,goal) <= super.getEntity().getRange();
    }


    private void performDefaultAction() {
        if (hasPatrolSet()){
            moveAlongPatrol();
        }
    }

    private void setOriginalState() {
        originalState = false;
    }

    private void getToOriginalState(Point3D position) {
        if (isAtOrigin(position)){
            resetOriginalState();
        }
        else{
            moveToOrigin(position);
        }
    }

    private void resetOriginalState() {
        originalState = true;
    }

    private boolean isInOriginalState() {
        return originalState;
    }

    private void moveToOrigin(Point3D position) {
        moveToGoal(position,origin);
    }

    private boolean hasPatrolSet() {
        return patrolPath != null;
    }

    private boolean isAtOrigin(Point3D currentPosition) {
        return currentPosition.distance(origin) == 0;
    }

    private Point3D getTarget(Point3D position){
        return getNearestTarget(position);
    }

    private boolean isReachable(Point3D position, Point3D goal, Entity ent){
        ArrayList<Point3D> reachable = pathCalculator.getReachablePoints(position, (int) chaseRadius, ent);
        return reachable.contains(goal);
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
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
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
            distance = HexDistanceCalculator.getHexDistance(origin,point);
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

    @Override
    public boolean wantToTalk(){
        return false;
    }
}
