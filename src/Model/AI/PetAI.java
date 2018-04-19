package Model.AI;

import Model.Entity.Entity;
import Model.Item.Item;
import Model.Utility.BidiMap;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.*;

import static Model.AI.PetPriority.*;

public class PetAI extends AIState {
    private BidiMap<Point3D, Entity> entityMap;
    private Map<Point3D, Item> itemMap;
    private Entity player;
    private PetPriority priority;
    private PathingAlgorithm pathCalculator;
    private List<Entity> targetList;


    public PetAI(Entity ent, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap, Entity player, List<Entity> TargetingList) {
        super(ent);
        this.entityMap = entityMap;
        this.itemMap = itemMap;
        this.player = player;
        priority = NONE;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        this.targetList = TargetingList;
    }

    @Override
    public void nextMove() {
        Point3D petPoint = getPetPoint(); //TODO: Implement a "case statement" that handles commands to Pet to do different actions based on priority
        Point3D nearestItem = getNearestItem(petPoint);
        Point3D nearestTarget = getNearestTarget(petPoint);
        Point3D playerPoint = getPlayerPoint();

        Point3D goal = calculateGoal(petPoint, playerPoint, nearestItem, nearestTarget);
        moveToGoal(petPoint,goal);
    }

    private void moveToGoal(Point3D start, Point3D goal){
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, player);
        Point3D firstStep = path.get(0);
        super.getEntity().addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D calculateGoal(Point3D origin, Point3D playerPoint, Point3D nearestItem, Point3D nearestEntity){
        double playerDistance = origin.distance(playerPoint);
        double itemDistance = origin.distance(nearestItem);
        double entityDistance = origin.distance(nearestEntity);

        double goalDistance = findShortestDistance(playerDistance,itemDistance,entityDistance);
        if (goalDistance == entityDistance){
            return nearestEntity;
        }
        else if (goalDistance == itemDistance){
            return nearestItem;
        }
       else {
            return playerPoint;
        }
    }

    private double findShortestDistance(double pDist, double iDist, double eDist){
        if (pDist == iDist && pDist == eDist){
            if (priority == PLAYER){
                return pDist;
            }
            else if (priority == ITEMS){
                return iDist;
            }
            else if (priority == ENEMIES){
                return eDist;
            }
            else {
                Random rand = new Random();
                ArrayList<Double> holder = new ArrayList<>();
                holder.add(pDist);
                holder.add(eDist);
                holder.add(iDist);
                return holder.get(rand.nextInt(2));
            }
        }
        //TODO: Other cases for priority if the need/time arises
        return Math.min(pDist,Math.min(iDist,eDist));
    }


    private Point3D getNearestItem(Point3D origin) {
        List<Point3D> itemPoints = new ArrayList<>(itemMap.keySet());
        Point3D minLocation = itemPoints.get(0);
        double minDistance = Double.MAX_VALUE;
        double distance;
        for (Point3D point : itemPoints) {
            distance = origin.distance(point);
            if (distance < minDistance) {
                minDistance = distance;
                minLocation = point;
            }
        }
        return minLocation;
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

    private Point3D getPetPoint() {
        return entityMap.getKeyFromValue(super.getEntity());
    }

    private Point3D getPlayerPoint() {
        return entityMap.getKeyFromValue(player);
    }

    public PetPriority getPriority() {
        return priority;
    }

    public void setPriority(PetPriority priority) {
        this.priority = priority;
    }

    public void addTarget(Entity ent){
        targetList.add(ent);
    }

    public void removeTarget(Entity ent){
        targetList.remove(ent);
    }
}
