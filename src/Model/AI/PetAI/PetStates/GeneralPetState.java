package Model.AI.PetAI.PetStates;

import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.AI.PetAI.PetPriority;
import Model.Entity.Entity;
import Model.Item.Item;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import Model.Utility.HexDistanceCalculator;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static Model.AI.PetAI.PetPriority.*;

public class GeneralPetState extends AIState {
    private BidiMap<Point3D, Entity> entityMap;
    private Map<Point3D, Item> itemMap;
    private Entity player;
    private PetPriority priority;
    private PathingAlgorithm pathCalculator;
    private List<Entity> targetList;


    public GeneralPetState(Entity pet, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap, Entity player) {
        super(pet);
        this.entityMap = entityMap;
        this.itemMap = itemMap;
        this.player = player;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        this.targetList = pet.getTargetingList();
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
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
        super.getEntity().addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D calculateGoal(Point3D origin, Point3D playerPoint, Point3D nearestItem, Point3D nearestEntity){
        double playerDistance = HexDistanceCalculator.getHexDistance(origin,playerPoint);
        double itemDistance = HexDistanceCalculator.getHexDistance(origin,nearestItem);
        double entityDistance = HexDistanceCalculator.getHexDistance(origin,nearestEntity);

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
        for (Point3D point : itemPoints) {
            double distance = HexDistanceCalculator.getHexDistance(origin,point);
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
        for (Point3D point : targetPoints) {
            if (!entityMap.getValueFromKey(point).equals(player) && !entityMap.getValueFromKey(point).equals(super.getEntity())) { //TODO: Possible LoD violation?
                double distance = HexDistanceCalculator.getHexDistance(origin,point);
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
}
