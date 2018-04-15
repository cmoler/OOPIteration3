package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import javafx.geometry.Point3D;

import java.util.Map;

public abstract class AIState {

    public AIState(Entity ent){
        entity = ent;
    }

    private Entity entity;

    public abstract void nextMove(Entity player, Map<Point3D,Terrain> terrainMap, Map<Point3D,Entity> entityMap, Map<Point3D,Obstacle> obstacleMap);

    public Entity getEntity() {
        return entity;
    }
}
