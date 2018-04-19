package Model.AI.PetAI;

import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// In CombatState, a pet will frenzy and won't stop until it kills its enemies on the map!

public class CombatPetState implements PetState{
    private BidiMap<Point3D, Entity> entityMap;
    private PathingAlgorithm pathCalculator;
    private List<Entity> targetList;


    public CombatPetState(Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player, List<Entity> TargetingList) {
        this.entityMap = entityMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
        this.targetList = TargetingList;
    }

    @Override
    public void nextPetMove(Entity pet) {
        if (hasNoTarget()){
            return;
        }

        Point3D petPoint = getPetPoint(pet);
        Point3D nearestTarget = getNearestTarget(petPoint,pet);

        moveToGoal(pet,petPoint,nearestTarget);
    }

    private boolean hasNoTarget() {
        return targetList.size() == 0;
    }

    private void moveToGoal(Entity pet, Point3D start, Point3D goal){
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, pet);
        Point3D firstStep = path.get(0);
        pet.addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getNearestTarget(Point3D origin, Entity pet) {
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

    private Point3D getPetPoint(Entity pet) {
        return entityMap.getKeyFromValue(pet);
    }

    public void addTarget(Entity ent){
        targetList.add(ent);
    }

    public void removeTarget(Entity ent){
        targetList.remove(ent);
    }
}