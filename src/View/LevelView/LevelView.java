package View.LevelView;

import Configs.Commons;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Level.Level;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        Map<Point3D, InfluenceEffect> influenceEffects = currentLevel.getInfluenceEffectMap();

        List<LevelViewElement> influenceEffectViews = new ArrayList<>();

        for(Point3D point3D : influenceEffects.keySet()) {
            influenceEffectViews.add(new InfluenceEffectView(influenceEffects.get(point3D), point3D));
        }

     //   for(InfluenceEffect influenceEffect : influenceEffects.values()) {
    //        influenceEffectViews.add(new InfluenceEffectView(influenceEffect, influenceEffects.key));
    //    }

        for(LevelViewElement influenceEffectView : influenceEffectViews) {
            influenceEffectView.render(gc, offset, scrollOffset);
        }

        Iterator itr = observationViews.iterator();
        while (itr.hasNext()) {//Render observation windows
            ObservationView observationView = (ObservationView)itr.next();
            observationView.setPosition(currentLevel.getEntityPoint(observationView.getEntity()));
            observationView.locationViewedByPlayer();
            observationView.render(gc, offset, scrollOffset);

            if(observationView.readyToBeRemoved()) {
                itr.remove();
            }
        }

        hudStatsView.render(gc);
        hotbarView.render(gc);
    }

    public void setCurrentLevel(Level currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Level getCurrentLevel() {
        return currentLevel;
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

   /* public void refreshInfluenceEffectSprites() {
        currentLevel.clearInfluenceEffectObservers();

        createInfluenceEffectObservers(currentLevel.getInfluenceEffectMap());

        currentLevel.addInfluenceEffectObservers();
    }*/

    /*private ArrayList<LevelViewElement> createInfluenceEffectObservers(Map<Point3D, InfluenceEffect> influenceEffectMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : influenceEffectMap.keySet()) {
            InfluenceEffect effect = influenceEffectMap.get(point);

            InfluenceEffectView observer = new InfluenceEffectView(effect, point);

            observers.add(observer);
        }

        return observers;
    }*/
}
