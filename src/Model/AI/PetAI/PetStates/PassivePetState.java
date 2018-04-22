package Model.AI.PetAI.PetStates;

import Controller.Visitor.SavingVisitor;
import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.River;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.Map;

// In PassiveState, a pet will only follow its master!

public class PassivePetState extends AIState {
    private BidiMap<Point3D, Entity> entityMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;


    public PassivePetState(Entity pet, Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player, Map<Point3D, River> riverMap) {
        super(pet);
        this.entityMap = entityMap;
        this.player = player;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap,riverMap , entityMap);
    }

    @Override
    public void nextMove() {
        Point3D petPoint = getPetPoint();
        Point3D playerPoint = getPlayerPoint();

        moveToGoal(petPoint,playerPoint);
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitPassivePetState(this);
    }

    private void moveToGoal(Point3D start, Point3D goal){
        Point3D firstStep = pathCalculator.getAStarPoint(start, goal, super.getEntity());
        super.getEntity().addVelocityFromControllerInput(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getPetPoint() {
        return entityMap.getKeyFromValue(super.getEntity());
    }

    private Point3D getPlayerPoint() {
        return entityMap.getKeyFromValue(player);
    }
}
