package View.LevelView;

import javafx.scene.canvas.GraphicsContext;

import javafx.geometry.Point2D;

public interface LevelViewElement {

    void notifyViewElement();
    void render(GraphicsContext gc, Point2D offset);
}
