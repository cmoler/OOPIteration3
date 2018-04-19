package View.MenuView;

import javafx.scene.canvas.GraphicsContext;

public class MenuView {

    private MenuViewState activeState;

    public void setActiveState(MenuViewState menuViewState){
        this.activeState = menuViewState;
    }

    public void render(GraphicsContext gc){
        activeState.render(gc);
    }
}
