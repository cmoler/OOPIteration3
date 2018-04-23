package Model.MenuModel;

import Controller.Factories.PetAIFactory;
import Controller.GameLoop;
import Model.AI.AIState;
import Model.AI.PetAI.PetPriority;
import Model.AI.PetAI.PetStates.CombatPetState;
import Model.Entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class PetMenu extends InGameMenuState {

    private int selectedPet;
    private int selectedFocus;
    private int selectedPriority;
    private List<Entity> pets;

    public PetMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
        this.selectedPet = 0;
        this.selectedFocus = 0;
        this.selectedPriority = 0;
        this.pets = player.getFriendlyList();
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 2;
        if(selectedLeftRight > 2) selectedLeftRight = 0;
        if(selectedLeftRight == 0){
            if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
            if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
        }else if(selectedFocus == 0){ // 0 is general
            if(selectedUpDown < 0) selectedUpDown = 2;
            if(selectedUpDown > 2) selectedUpDown = 0;
        }else{
            if(selectedUpDown < 0) selectedUpDown = 1;
            if(selectedUpDown > 1) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        if(selectedLeftRight == 0) {
            inGameMenuBar.select(selectedUpDown);
            return;
        }
        switch (selectedUpDown){
            case 0:
                petPart();
                break;
            case 1:
                focusPart();
                break;
            case 2:
                priorityPart();
                break;
        }
    }

    private void petPart() {
        switch (selectedLeftRight){
            case 1:
                selectedPet--;
                break;
            case 2:
                selectedPet++;
                break;
        }
        if(selectedPet < 0) selectedPet = pets.size() - 1;
        else if(selectedPet > pets.size() - 1) selectedPet = 0;
    }

    private void focusPart() {
        switch (selectedLeftRight){
            case 1:
                selectedFocus--;
                break;
            case 2:
                selectedFocus++;
                break;
        }
        if(selectedFocus < 0) selectedFocus = 3;
        if(selectedFocus > 3) selectedFocus = 0;
        AIState aiState = null;
        PetAIFactory petAIFactory = gameLoop.getPetAIFactoryFromCurrentLevel();
        switch (selectedFocus){
            case 0:
                aiState = petAIFactory.getGeneralPetState();
                break;
            case 1:
                aiState = petAIFactory.getCombatPetState();
                break;
            case 2:
                aiState = petAIFactory.getItemPetState();
                break;
            case 3:
                aiState = petAIFactory.getPassivePetState();
                break;
        }
        if(pets != null && pets.get(selectedPet) != null) gameLoop.setAIOnCurrentLevel(pets.get(selectedPet), aiState);
    }

    private void priorityPart() {
        switch (selectedLeftRight){
            case 1:
                selectedPriority--;
                break;
            case 2:
                selectedPriority++;
                break;
        }
        if(selectedPriority < 0) selectedPriority = 3;
        if(selectedPriority > 3) selectedPriority = 0;
        switch (selectedPriority){
            case 0:
                if(pets != null && pets.get(selectedPet) != null) gameLoop.setAIPriorityOnCurrentLevel(pets.get(selectedPet), PetPriority.ITEMS);
                break;
            case 1:
                if(pets != null && pets.get(selectedPet) != null) gameLoop.setAIPriorityOnCurrentLevel(pets.get(selectedPet), PetPriority.PLAYER);
                break;
            case 2:
                if(pets != null && pets.get(selectedPet) != null) gameLoop.setAIPriorityOnCurrentLevel(pets.get(selectedPet), PetPriority.ENEMIES);
                break;
            case 3:
                if(pets != null && pets.get(selectedPet) != null) gameLoop.setAIPriorityOnCurrentLevel(pets.get(selectedPet), PetPriority.NONE);
                break;
        }
    }

    public int getSelectedPet(){
        return selectedPet;
    }

    public int getSelectedFocus(){
        return selectedFocus;
    }

    public int getSelectedPriority(){
        return selectedPriority;
    }

    public List<Entity> getPets() {
        return pets;
    }
}
