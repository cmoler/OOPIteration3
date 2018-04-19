package Model.AI.PetAI;

import Model.AI.AIState;
import Model.AI.PetAI.PetStates.PetState;
import Model.Entity.Entity;


public abstract class PetAI extends AIState {
    private PetState currentState;

    public PetAI(Entity ent) {
        super(ent);
    }

    public void nextMove(){
        currentState.nextPetMove(super.getEntity());
    }

    public void setCurrentState(PetState petState){
        currentState = petState;
    }
}
