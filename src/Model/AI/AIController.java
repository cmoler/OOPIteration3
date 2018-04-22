package Model.AI;

import Model.Entity.Entity;

public class AIController {

    private AIState activeState;

    public AIController(){

    }

    public void setActiveState(AIState activeState) {
        this.activeState = activeState;
    }

    public AIState getActiveState(){
        return activeState;
    }

    public boolean controlsEntity(Entity entity) {
        if (activeState != null){
            return getEntity().equals(entity);
        }
        return false;
    }

    public Entity getEntity(){
        return activeState.getEntity();
    }

    public void processMove(){
        activeState.nextMove();
    }

    public boolean wantToTalk(){
        return activeState.wantToTalk();
    }
}
