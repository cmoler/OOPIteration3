package Model.AI;

import Model.Entity.Entity;

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
