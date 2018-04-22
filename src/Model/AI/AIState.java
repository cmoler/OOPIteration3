package Model.AI;

import Controller.Visitor.SavingVisitor;
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

    public boolean wantToTalk(){
        return true;
    }

    public abstract void accept(SavingVisitor visitor);
}
