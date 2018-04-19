package View;

import Model.Level.Level;
import View.LevelView.HUDStatsView;
import View.LevelView.HotbarView;
import View.LevelView.LevelView;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import View.MenuView.TitleScreenView;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {

    private LevelView levelView;

    private MenuView menuView;

    public Renderer() {
        levelView = new LevelView();

        menuView = new MenuView();

    }


    public void render(GraphicsContext gc, Point3D playerPos) {
        if(menuView.inMenu()){
            menuView.render(gc);
        }
        else{
            levelView.render(gc, playerPos);
        }

    }

    public void updateCurrentLevel(Level newCurrentLevel) {
        levelView.setCurrentLevel(newCurrentLevel);
    }

    public void setActiveMenuState(MenuViewState menuViewState) {
        menuView.setActiveState(menuViewState);
        menuView.setInMenu(true);
    }

    public void closeMenu() {
        menuView.setInMenu(false);
    }
}
