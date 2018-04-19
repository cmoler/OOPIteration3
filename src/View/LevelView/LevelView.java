package View.LevelView;

import Configs.Commons;
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

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
        for(int i = 4; i >= 0; i--) {
            for (LevelViewElement o : observers) {
                if(o.getRenderPriority() == i) {//Ensures level objects are rendered in the correct order
                    o.render(gc, offset);
                }
            }
        }
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }
}
