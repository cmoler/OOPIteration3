package Model.AI.PetAI.PetStates;

import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
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

// In ItemState, a super.getEntity() will disregard its master and run wildly towards items and Entities in hopes to grab all the loot!

public class ItemPetState extends AIState {
    private BidiMap<Point3D, Entity> entityMap;
    private Map<Point3D, Item> itemMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;
    private Skill pickPocketSkill;


    public ItemPetState(Entity pet, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap, Entity player, Skill PickPocketSkill) {
        super(pet);
        this.entityMap = entityMap;
        this.itemMap = itemMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        pickPocketSkill = PickPocketSkill;
    }

    @Override
    public void nextMove() {
        if (!hasItemsToLoot()){
            return;
        }

        Point3D petPoint = getPetPoint();
        Point3D nearestItem = getNearestItem(petPoint);
        Point3D nearestTarget = getNearestEntity(petPoint);

        if(isInStealingRange(petPoint,nearestItem) && targetHasItemsToSteal(nearestItem)){
            // TODO: Issue command to steal
            executePickpocket(super.getEntity());
        }
        else{
            Point3D goal = calculateGoal(petPoint, nearestItem, nearestTarget);
            moveToGoal(petPoint,goal);
        }
    }

    private void executePickpocket(Entity pet) {
        pet.useSkill(pickPocketSkill);
    }

    private boolean hasItemsToLoot(){
        return itemMap.size() >= 1;
    }

    private boolean targetHasItemsToSteal(Point3D nearestItem) {
        Entity target = entityMap.getValueFromKey(nearestItem);
        return target.hasItems();
    }

    private boolean isInStealingRange(Point3D petPoint, Point3D nearestTarget){
        return petPoint.distance(nearestTarget) == 1;
    }

    private void moveToGoal(Point3D start, Point3D goal){
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
        super.getEntity().addVelocityFromControllerInput(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D calculateGoal(Point3D origin, Point3D nearestItem, Point3D nearestEntity){
        double itemDistance = HexDistanceCalculator.getHexDistance(origin,nearestItem);
        double entityDistance = HexDistanceCalculator.getHexDistance(origin,nearestEntity);

        double goalDistance = findShortestDistance(itemDistance,entityDistance);
        if (goalDistance == entityDistance){
            return nearestEntity;
        }
        else {
            return nearestItem;
        }
    }

    private double findShortestDistance(double iDist, double eDist){
        if (iDist == eDist){
            return iDist;
        }
        else {
            Random rand = new Random();
            ArrayList<Double> holder = new ArrayList<>();
            holder.add(eDist);
            holder.add(iDist);
            return holder.get(rand.nextInt(holder.size()));
        }
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

    private Point3D getNearestEntity(Point3D origin) {
        Point3D minLocation = new Point3D(0,0,0);
        double minDistance = Double.MAX_VALUE;
        for (Point3D point : entityMap.getKeyList()) {
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

    private Point3D getPetPoint() {
        return entityMap.getKeyFromValue(super.getEntity());
    }
}
