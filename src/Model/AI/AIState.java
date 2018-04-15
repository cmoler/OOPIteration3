package Model.AI;

import Model.Entity.Entity;
import Model.Level.Level;

public abstract class AIState {

    private Entity entity;

    public abstract void nextMove(Level level);

    public Entity getEntity() {
        return entity;
    }
}
