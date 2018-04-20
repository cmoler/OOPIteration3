package Model.Level;

import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MovementHandler {
    private Map<Point3D,Terrain> terrainLocations;
    private Map<Point3D, Obstacle> obstacleLocations;
    private BidiMap<Point3D, Entity> entityLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;

    public MovementHandler(Map<Point3D, Terrain> terrainLocations,
                           Map<Point3D, Obstacle> obstacleLocations,
                           BidiMap<Point3D, Entity> entityLocations,
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
        for (Entity entity : entityLocations.getValueList()) { //For each entry in the map
            Point3D entityPoint = entityLocations.getKeyFromValue(entity);
            if (entity.isMoving()){
                Point3D contestedPoint = calculateMove(entityPoint, entity.getVelocity());
                if (!obstacleLocations.containsKey(contestedPoint) && entity.canMoveOnTerrain(terrainLocations.get(contestedPoint))){
                    if (entityLocations.hasKey(contestedPoint)){
                        // TODO: Figure out this use case.
                        System.out.println("Hello Entity! I, another Entity, is interacting with you!");
                    } else {
                        //Update entity movement
                        entityLocations.removeByKey(entityPoint);
                        entityLocations.place(contestedPoint, entity);

                        if (entity.isMounted()) {
                            Mount mount = mountLocations.get(entityPoint);
                            mountLocations.remove(entityPoint);
                            mount.setOrientation(entity.getOrientation());
                            mountLocations.put(contestedPoint, mount);
                            mount.notifyObservers(contestedPoint);
                        }
                        if (mountLocations.containsKey(contestedPoint) && !entity.isMounted()) {
                            entity.mountVehicle(mountLocations.get(contestedPoint));
                        }

                        entity.notifyObservers(contestedPoint);
                    }
                }
                entity.decrementVelocity();
            }
        }
    }

    private void moveInfluenceEffects() { // TODO: notify influence effect observers on position changed
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