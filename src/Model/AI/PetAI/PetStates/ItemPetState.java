package Model.AI.PetAI.PetStates;

import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.Item;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

// In ItemState, a pet will disregard its master and run wildly towards items and Entities in hopes to grab all the loot!

public class ItemPetState implements PetState {
    private BidiMap<Point3D, Entity> entityMap;
    private Map<Point3D, Item> itemMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;
    private Skill pickPocketSkill;


    public ItemPetState(Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap, Entity player, Skill PickPocketSkill) {
        this.entityMap = entityMap;
        this.itemMap = itemMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        pickPocketSkill = PickPocketSkill;
    }

    @Override
    public void nextPetMove(Entity pet) {
        if (!hasItemsToLoot()){
            return;
        }

        Point3D petPoint = getPetPoint(pet);
        Point3D nearestItem = getNearestItem(petPoint);
        Point3D nearestTarget = getNearestEntity(petPoint,pet);

        if(isInStealingRange(petPoint,nearestItem) && targetHasItemsToSteal(nearestItem)){
            // TODO: Issue command to steal
            executePickpocket(pet);
        }
        else{
            Point3D goal = calculateGoal(petPoint, nearestItem, nearestTarget);
            moveToGoal(pet,petPoint,goal);
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

    private void moveToGoal(Entity pet, Point3D start, Point3D goal){
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, pet);
        pet.addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D calculateGoal(Point3D origin, Point3D nearestItem, Point3D nearestEntity){
        double itemDistance = origin.distance(nearestItem);
        double entityDistance = origin.distance(nearestEntity);

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

    private Point3D getNearestEntity(Point3D origin, Entity pet) {
        Point3D minLocation = new Point3D(0,0,0);
        double minDistance = Double.MAX_VALUE;
        double distance;
        for (Point3D point : entityMap.getKeyList()) {
            if (!entityMap.getValueFromKey(point).equals(player) && !entityMap.getValueFromKey(point).equals(pet)) { //TODO: Possible LoD violation?
                distance = origin.distance(point);
                if (distance < minDistance) {
                    minDistance = distance;
                    minLocation = point;
                }
            }
        }
        return minLocation;
    }

    private Point3D getPetPoint(Entity pet) {
        return entityMap.getKeyFromValue(pet);
    }
}
