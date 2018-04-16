package Model.MenuModel;

public class MenuModel {

    private MenuState currentState;

    public MenuModel(){
        this.currentState = new MainMenuState(this);
    }

    public void setActiveState(MenuState menuState){
        this.currentState = menuState;
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
}
