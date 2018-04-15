package Model.AI;

import Model.Entity.Entity;

import java.util.Random;

public class FriendlyAI extends AIState{

    public FriendlyAI(Entity ent) {
        super(ent);
    }

    @Override
    public void nextMove() {
        if (getEntity().isMoveable()) {
            Random rand = new Random();
            super.getEntity().addVelocity(super.generateRandomVelcity());
        }
    }

}
