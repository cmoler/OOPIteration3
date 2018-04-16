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

    public Vec3d generateRandomVelcity() {
        Random rand = new Random();
        ArrayList<Vec3d> moves = new ArrayList<>();
        moves.add(new Vec3d(0, 1, -1));
        moves.add(new Vec3d(0, -1, 1));
        moves.add(new Vec3d(1, 0, -1));
        moves.add(new Vec3d(-1, 0, 1));
        moves.add(new Vec3d(-1, 1, 0));
        moves.add(new Vec3d(1, -1, 0));
        return moves.get(rand.nextInt(moves.size()));

    }
}
