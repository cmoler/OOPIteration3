package Model.AI.PetAI.PetStates;

import Controller.Visitor.SavingVisitor;
import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
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

// In CombatState, a pet will frenzy and won't stop until it kills its enemies on the map!

public class CombatPetState extends AIState {
    private BidiMap<Point3D, Entity> entityMap;
    private PathingAlgorithm pathCalculator;
    private List<Entity> targetList;
    private Entity player;


    public CombatPetState(Entity pet, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player, Map<Point3D, River> riverMap) {
        super(pet);
        this.entityMap = entityMap;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap,riverMap ,entityMap );
        this.targetList = pet.getTargetingList();
        this.player = player;
    }

    @Override
    public void nextMove() {

        Point3D petPoint = getPetPoint();
        if (hasNoTarget()){
            moveToGoal(petPoint,getEntityPoint(player,entityMap));
        }
        else {
            Point3D nearestTarget = getNearestTarget(petPoint);

            if (isInAttackRange(petPoint, nearestTarget)) {
                attack(petPoint, nearestTarget);
            } else {
                moveToGoal(petPoint, nearestTarget);
            }
        }
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitCombatPetState(this);
    }

    private void attack(Point3D position, Point3D goal) {
        super.getEntity().setOrientation(PathingAlgorithm.calculateOrientation(position,goal));
        super.getEntity().attack();
    }

    private boolean isInAttackRange(Point3D position, Point3D goal) {
        return HexDistanceCalculator.getHexDistance(position,goal) <= super.getEntity().getRange();
    }

    private boolean hasNoTarget() {
        return targetList.size() == 0;
    }

    private void moveToGoal(Point3D start, Point3D goal){
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
        super.getEntity().addVelocityFromControllerInput(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getNearestTarget(Point3D origin) {
        List<Point3D> targetPoints = getTargetPoints();
        Point3D minLocation = targetPoints.get(0);
        double minDistance = Double.MAX_VALUE;
        for (Point3D point : targetPoints) {
            double distance = HexDistanceCalculator.getHexDistance(origin,point);
            if (distance < minDistance) {
                minDistance = distance;
                minLocation = point;
            }
        }
        return minLocation;
    }

    private Point3D getEntityPoint(Entity entity, BidiMap<Point3D, Entity> entityLocations) {
        return entityLocations.getKeyFromValue(entity);
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
}