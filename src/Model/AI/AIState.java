package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;
import Model.Level.Obstacle;
import Model.Level.Terrain;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public abstract class AIState {

    public AIState(Entity ent){
        entity = ent;
    }

    private Entity entity;

    public abstract void nextMove();

    public Entity getEntity() {
        return entity;
    }
}
