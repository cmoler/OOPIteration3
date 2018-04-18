package View.LevelView;

import Model.Level.Level;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.Canvas;

import java.util.List;

public class LevelView {


    private Level currentLevel;
    private HexMathHelper hexMathHelper;

    public LevelView() {


        hexMathHelper = new HexMathHelper();
    }

    public void render(GraphicsContext gc, Point3D playerPos) {
        Canvas canvas = gc.getCanvas();
        int playerOffsetX = hexMathHelper.getXCoord(playerPos);
        int playerOffsetY = hexMathHelper.getYCoord(playerPos);
        //TODO modify offset if user is scrolling viewport
        Point2D offset = new Point2D((canvas.getWidth()/2)-playerOffsetX, (canvas.getHeight()/2)-playerOffsetY);

        List<LevelViewElement> observers = currentLevel.getObservers();
        for(LevelViewElement o:observers) {
            o.render(gc, offset);
        }
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }
}
