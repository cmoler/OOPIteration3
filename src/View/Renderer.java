package View;

import View.LevelView.LevelView;
import View.MenuView.MenuView;
import View.TitleScreenView.TitleScreenView;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
    private GraphicsContext gc;
    private LevelView levelView;
    private MenuView menuView;
    private TitleScreenView titleScreenView;

    public Renderer(GraphicsContext gc) {
        this.gc = gc;
    }


    public void render() {
        levelView.render();
    }
}
