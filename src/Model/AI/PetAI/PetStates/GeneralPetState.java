package Model.AI.PetAI.PetStates;

import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.AI.PetAI.PetPriority;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.Item;
import Model.Level.Obstacle;
import Model.Level.River;
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
    private Skill pickPocketSkill;


    public GeneralPetState(Entity pet, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap, Skill pickpock, Entity player, Map<Point3D, River> riverMap) {
        super(pet);
        this.entityMap = entityMap;
        this.itemMap = itemMap;
        this.player = player;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap,riverMap,entityMap );
        this.targetList = pet.getTargetingList();
        pickPocketSkill = pickpock;
    }

    @Override
    public void nextMove() {
        Point3D petPoint = getPetPoint(); //TODO: Implement a "case statement" that handles commands to Pet to do different actions based on priority

        Point3D nearestItem = getNearestItem(petPoint);
        Point3D nearestSteal = getNearestStealingTarget(petPoint);
        Point3D nearestTarget = getNearestTarget(petPoint);
        Point3D playerPoint = getPlayerPoint();

        if (hasNoTargets(nearestItem,nearestSteal,nearestTarget)){
            System.out.println("Moving to Player...");
            moveToGoal(petPoint,playerPoint);
        }
        else {
            Point3D goal = calculateGoal(petPoint, playerPoint, nearestItem, nearestSteal, nearestTarget);
            if (isInActionRange(petPoint,goal)){
                if (goal.equals(nearestTarget)){
                    System.out.println("Attacking");
                    attack(petPoint, nearestTarget);
                }
                else if (goal.equals(nearestSteal)){
                    System.out.println("Stealing");
                    executePickpocket(super.getEntity());
                }
                else{
                    moveToGoal(petPoint, goal);
                }
            }
            else {
                //System.out.println("Simply moving");
                moveToGoal(petPoint, goal);
            }
        }
    }

    private void attack(Point3D position, Point3D goal) {
        super.getEntity().setOrientation(PathingAlgorithm.calculateOrientation(position,goal));
        super.getEntity().attack();
    }

    private void executePickpocket(Entity pet) {
        pet.useSkill(pickPocketSkill);
    }

    private boolean isInActionRange(Point3D origin, Point3D goal) {
        return HexDistanceCalculator.getHexDistance(origin,goal) == 1;
    }

    private boolean hasNoTargets(Point3D nearestItem, Point3D nearestSteal, Point3D nearestTarget) {
        Point3D dummy = new Point3D(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        return (dummy.equals(nearestItem) && dummy.equals(nearestSteal) && dummy.equals(nearestTarget));
    }

    private void moveToGoal(Point3D start, Point3D goal){
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
        super.getEntity().addVelocityFromControllerInput(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D calculateGoal(Point3D origin, Point3D playerPoint, Point3D nearestItem, Point3D nearestSteal, Point3D nearestEntity){
        double itemDistance = HexDistanceCalculator.getHexDistance(origin,nearestItem);
        double stealDistance = HexDistanceCalculator.getHexDistance(origin,nearestSteal);
        double entityDistance = HexDistanceCalculator.getHexDistance(origin,nearestEntity);

        double goalDistance = findShortestDistance(itemDistance, stealDistance, entityDistance);
        if (goalDistance == entityDistance){
            return nearestEntity;
        }
        else if (goalDistance == stealDistance){
            return nearestSteal;
        }
        else if (goalDistance == itemDistance){
            return nearestItem;
        }
        else {
            return playerPoint;
        }
    }

    private double findShortestDistance(double iDist, double sDist, double eDist){
        if (iDist == eDist && eDist == sDist){
            if (priority == ITEMS){
                return iDist;
            }
            else if (priority == ENEMIES){
                return eDist;
            }
            else {
                Random rand = new Random();
                ArrayList<Double> holder = new ArrayList<>();
                holder.add(-1.00);
                holder.add(eDist);
                holder.add(sDist);
                holder.add(iDist);
                return holder.get(rand.nextInt(3));
            }
        }
        //TODO: Other cases for priority if the need/time arises
        return Math.min(iDist,eDist);
    }


    private Point3D getNearestItem(Point3D origin) {
        List<Point3D> itemPoints = new ArrayList<>(itemMap.keySet());
        Point3D minLocation = new Point3D(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
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

    private Point3D getNearestStealingTarget(Point3D origin) {
        Point3D minLocation = new Point3D(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
        double minDistance = Double.MAX_VALUE;
        for (Point3D point : entityMap.getKeyList()) {
            Entity entity = getEntityFromPoint(point);
            if (!entity.equals(player) && entity.hasItems() && !entity.equals(super.getEntity())) {
                double distance = HexDistanceCalculator.getHexDistance(origin,point);
                if (distance < minDistance) {
                    minDistance = distance;
                    minLocation = point;
                }
            }
        }
        return minLocation;
    }

    private Point3D getNearestTarget(Point3D origin) {
        List<Point3D> targetPoints = getTargetPoints();
        Point3D minLocation = new Point3D(Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE);
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

    private Entity getEntityFromPoint(Point3D point) {
        return entityMap.getValueFromKey(point);
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
