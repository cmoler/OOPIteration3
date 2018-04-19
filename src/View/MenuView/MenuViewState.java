package View.MenuView;

import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;

public abstract class MenuViewState {

    protected MenuModel menuModel;

    public MenuViewState(MenuModel menuModel){
        this.menuModel = menuModel;
    }

    public abstract void render(GraphicsContext gc);
}
