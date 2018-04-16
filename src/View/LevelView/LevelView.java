package View.LevelView;

import Model.Level.Level;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public class LevelView {
    private GraphicsContext gc;
    private Level currentLevel;

    public LevelView(GraphicsContext gc) {
        this.gc = gc;

    }

    public void render() {
        List<LevelViewElement> observers = currentLevel.getObservers();
        for(LevelViewElement o:observers) {
            o.render(gc);
        }
    }
}
