package Model.MenuModel;

public abstract class MenuState {

    private int selectedLeftRight;
    private int selectedUpDown;

    public abstract void scrollLeft();

    public abstract void scrollRight();

    public abstract void scrollUp();

    public abstract void scrollDown();

    public abstract void select();
}
