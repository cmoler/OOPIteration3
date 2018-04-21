package View.LevelView;

import Configs.Commons;
import Model.Level.Level;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class LevelView {


    private Level currentLevel;
    private HexMathHelper hexMathHelper;
    private HUDStatsView hudStatsView;
    private HotbarView hotbarView;
    private List<ObservationView> observationViews;

    public LevelView() {


        hexMathHelper = new HexMathHelper();

        observationViews = new ArrayList<>();

    }

    public void render(GraphicsContext gc, Point3D playerPos, Point2D scrollOffset) {
        int playerOffsetX = hexMathHelper.getXCoord(playerPos);
        int playerOffsetY = hexMathHelper.getYCoord(playerPos);
        //TODO modify offset if user is scrolling viewport
        //Point2D offset = new Point2D((canvas.getWidth()/2)-playerOffsetX + scrollOffset.getX(), (canvas.getHeight()/2)-playerOffsetY + scrollOffset.getY());
        Point2D offset = new Point2D(playerOffsetX, playerOffsetY);

        List<LevelViewElement> observers = currentLevel.getObservers();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
        for(int i = 4; i >= 0; i--) {
            for (LevelViewElement o : observers) {
                if(o.getRenderPriority() == i) {//Ensures level objects are rendered in the correct order
                    o.render(gc, offset, scrollOffset);
                }
            }
        }



        for(ObservationView o: observationViews) {
            o.setPosition(currentLevel.getEntityPoint(o.getEntity()));
            o.locationViewedByPlayer();
            o.render(gc, offset, scrollOffset);
        }


        hudStatsView.render(gc);
        hotbarView.render(gc);
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }
    public void setPlayerHUD(HUDStatsView hud) {
        hudStatsView = hud;
    }
    public void setHotbarView(HotbarView hbv) {
        hotbarView = hbv;
    }

    public void addObservationView(ObservationView observationView) {
        observationViews.add(observationView);
    }
}
