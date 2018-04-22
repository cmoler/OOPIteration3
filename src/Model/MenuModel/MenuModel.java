package Model.MenuModel;

import Controller.GameLoop;

public class MenuModel {

    private MenuState currentState;

    public MenuModel(GameLoop gameLoop){
        this.currentState = new MainMenuState(this, gameLoop);
    }

    public void setToMainMenu(GameLoop gameLoop){
        currentState = new MainMenuState(this, gameLoop);
    }

    public void setActiveState(MenuState menuState){
        this.currentState = menuState;
    }

    public int getSelectedHorizontal(){
        return currentState.getHorizontal();
    }

    public int getSelectedVertical(){
        return currentState.getVertical();
    }

    public void scrollLeft(){
        currentState.scrollLeft();
    }

    public void scrollRight(){
        currentState.scrollRight();
    }

    public void scrollUp(){
        currentState.scrollUp();
    }

    public void scrollDown(){
        currentState.scrollDown();
    }

    public void select(){
        currentState.select();
    }

    public MenuState getActiveState() {
        return currentState;
    }
}
