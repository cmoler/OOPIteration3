package Model.MenuModel;

public class MenuModel {

    private MenuState currentState;

    public MenuModel(){
        this.currentState = new MainMenuState();
    }

    public void setActiveState(MenuState menuState){
        this.currentState = menuState;
    }

    public void scrollLeft(){

    }

    public void scrollRight(){

    }

    public void scrollUp(){

    }

    public void scrollDown(){

    }

    public void select(){

    }
}
