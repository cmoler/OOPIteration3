package Model.AI;

import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;

import java.util.Random;

public class FriendlyAI extends AIState{

    public FriendlyAI(Entity ent) {
        super(ent);
    }

    @Override
    public void nextMove() {
        if (getEntity().isMoveable()) {
            Random rand = new Random();
            super.getEntity().addVelocity(new Vec3d(rand.nextInt(1), rand.nextInt(1), rand.nextInt(1)));
        }
    }

}
