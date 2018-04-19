package Model.AI.PetAI.PetStates;

import Model.Entity.Entity;

public interface PetState {
    void nextPetMove(Entity pet);
}
