package Model.AI.PetAI.PetStates;

import Model.AI.PathingAlgorithm;
import Model.Entity.Entity;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Map;

// In PassiveState, a pet will only follow its master!

public class PassivePetState implements PetState {
    private BidiMap<Point3D, Entity> entityMap;
    private Entity player;
    private PathingAlgorithm pathCalculator;


    public PassivePetState(Map<Point3D, Terrain> terrainMap, BidiMap<Point3D, Entity> entityMap, Map<Point3D, Obstacle> obstacleMap, Entity player) {
        this.entityMap = entityMap;
        this.player = player;
        pathCalculator = new PathingAlgorithm(terrainMap,obstacleMap);
    }

    @Override
    public void nextPetMove(Entity pet) {
        Point3D petPoint = getPetPoint(pet);
        Point3D playerPoint = getPlayerPoint();

        moveToGoal(pet,petPoint,playerPoint);
    }

    private void moveToGoal(Entity pet, Point3D start, Point3D goal){
        ArrayList<Point3D> path = pathCalculator.getPath(start, goal, pet);
        Point3D firstStep = path.get(0);
        pet.addVelocity(new Vec3d(firstStep.getX()-start.getX(),firstStep.getY()-start.getY(),firstStep.getZ()-start.getZ()));
    }

    private Point3D getPetPoint(Entity pet) {
        return entityMap.getKeyFromValue(pet);
    }

    private Point3D getPlayerPoint() {
        return entityMap.getKeyFromValue(player);
    }
}
