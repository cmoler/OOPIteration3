package View.MenuView;

import javafx.scene.canvas.GraphicsContext;

public class MenuView {

    private MenuViewState activeState;
    private boolean inMenu;

    public void setActiveState(MenuViewState menuViewState){
        this.activeState = menuViewState;
    }

    public void render(GraphicsContext gc){
        activeState.render(gc);
    }

    public void setInMenu(boolean inMenu){
        this.inMenu = inMenu;
    }

    public boolean inMenu() {
        return inMenu;
    }
}
