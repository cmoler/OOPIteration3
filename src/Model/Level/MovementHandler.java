package Model.Level;

import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.Map;

public class MovementHandler {
    private Map<Point3D,Terrain> terrainLocations;
    private Map<Point3D, Obstacle> obstacleLocations;
    private Map<Point3D, Entity> entityLocations;
    private Map<Point3D, Mount> mountLocations;

    public MovementHandler(Map<Point3D, Terrain> terrainLocations, Map<Point3D, Obstacle> obstacleLocations, Map<Point3D, Entity> entityLocations, Map<Point3D, Mount> mountLocations) {
        this.terrainLocations = terrainLocations;
        this.obstacleLocations = obstacleLocations;
        this.entityLocations = entityLocations;
        this.mountLocations = mountLocations;
    }

    public void processMoves(){
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
                        System.out.println("Hello Entity!");
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
