package View.LevelView;

import javafx.scene.canvas.GraphicsContext;

public interface LevelViewElement {

    void notifyViewElement();
    void render(GraphicsContext gc);
}
