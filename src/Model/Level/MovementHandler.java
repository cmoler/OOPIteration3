package Model.Level;

import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovementHandler {
    private Map<Point3D,Terrain> terrainLocations;
    private Map<Point3D, Obstacle> obstacleLocations;
    private Map<Point3D, Entity> entityLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;

    public MovementHandler(Map<Point3D, Terrain> terrainLocations,
                           Map<Point3D, Obstacle> obstacleLocations,
                           Map<Point3D, Entity> entityLocations,
                           Map<Point3D, Mount> mountLocations,
                           Map<Point3D, InfluenceEffect> influenceEffectLocations) {
        this.terrainLocations = terrainLocations;
        this.obstacleLocations = obstacleLocations;
        this.entityLocations = entityLocations;
        this.mountLocations = mountLocations;
        this.influenceEffectLocations = influenceEffectLocations;
    }

    public void processMoves(){
        moveEntities();

        moveInfluenceEffects();
    }

    private void moveEntities() {
        for (Map.Entry<Point3D,Entity> entry: entityLocations.entrySet()) { //For each entry in the map
            Entity ent = entry.getValue();
            if (ent.isMoving()){
                Point3D contestedPoint = calculateMove(entry.getKey(), ent.getVelocity());
                if (!obstacleLocations.containsKey(contestedPoint) && ent.canMoveOnTerrain(terrainLocations.get(contestedPoint))){
                    if (mountLocations.containsKey(contestedPoint)){
                        ent.mountVehicle(mountLocations.get(contestedPoint));
                    }
                    if (entityLocations.containsKey(contestedPoint)){
                       //@TODO: Figure out this use case.
                        System.out.println("Hello Entity! I, another Entity, is interacting with you!");
                    }
                    else {
                        entityLocations.remove(entry.getKey());
                        entityLocations.put(contestedPoint, ent);

                        if (ent.isMounted()){
                            Mount mount = mountLocations.get(entry.getKey());
                            mountLocations.remove(entry.getKey());
                            mountLocations.put(contestedPoint,mount);
                        }
                    }
                }
                ent.decrementVelocity();
            }
        }
    }

    private void moveInfluenceEffects() {
        List<Point3D> influenceEffectPoints = new ArrayList<>(influenceEffectLocations.keySet());

        for(Point3D oldPoint : influenceEffectPoints) {
            InfluenceEffect influenceEffect = influenceEffectLocations.get(oldPoint); // Get current influence effect

            List<Point3D> nextEffectPoints = influenceEffect.nextMove(oldPoint); // Get list of points to move effect to
            influenceEffect.decreaseCommandAmount();

            if (!nextEffectPoints.isEmpty()) {
                influenceEffectLocations.remove(oldPoint, influenceEffect); // remove all old positions of the influence effect

                for (Point3D newPoint : nextEffectPoints) {
                    influenceEffectLocations.put(newPoint, influenceEffect); // put influence effect at its new position
                }
            }
        }
    }

    private Point3D calculateMove(Point3D startingPoint, Vec3d velocity){
        double newX = startingPoint.getX();
        double newY = startingPoint.getY();
        double newZ = startingPoint.getZ();

        if (Math.abs(velocity.x) > 0){ newX = newX+(velocity.x/Math.abs(velocity.x)); }

        if (Math.abs(velocity.y) > 0) { newY = newY+(velocity.y/Math.abs(velocity.y));}

        if (Math.abs(velocity.z)>0) { newZ = newZ+(velocity.z/Math.abs(velocity.z));}

        return new Point3D(newX,newY,newZ);
    }
}
