package Model.MenuModel;

public abstract class MenuState {

    protected int selectedLeftRight = 0;
    protected int selectedUpDown = 0;
    protected MenuModel menuModel;

    public MenuState(MenuModel menuModel) {
        this.menuModel = menuModel;
    }


    public void scrollLeft(){
        selectedLeftRight--;
        correctParameters();
    }

    public void scrollRight(){
        selectedLeftRight++;
        correctParameters();
    }

    public void scrollUp(){
        selectedUpDown++;
        correctParameters();
    }

    public void scrollDown(){
        selectedUpDown--;
        correctParameters();
    }

    public abstract void correctParameters();

    public abstract void select();
}
