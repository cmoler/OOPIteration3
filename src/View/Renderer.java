package View;


import Model.Level.Level;
import View.LevelView.HUDStatsView;
import View.LevelView.HotbarView;
import View.LevelView.LevelView;

import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import Model.Level.*;
import Model.Utility.BidiMap;
import View.LevelView.*;
import View.LevelView.EntityView.EntityView;

import View.MenuView.MenuView;
import View.MenuView.MenuViewState;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Renderer {

    private LevelView levelView;

    private MenuView menuView;

    List<TerrainView> terrainObservers;

    public Renderer() {
        levelView = new LevelView();

        menuView = new MenuView();

        terrainObservers = new ArrayList<>();
    }

    public void render(GraphicsContext gc, Point3D playerPos, Point2D scrollOffset) {
        if(menuView.inMenu()) {
            menuView.render(gc);
        } else {
            levelView.render(gc, playerPos, scrollOffset);
        }

    }

    public void updateCurrentLevel(Level newCurrentLevel) {
        levelView.setCurrentLevel(newCurrentLevel);
    }

    public void setActiveMenuState(MenuViewState menuViewState) {
        menuView.setActiveState(menuViewState);
        menuView.setInMenu(true);
    }


    public void setPlayerHUD(HUDStatsView hud) {
        levelView.setPlayerHUD(hud);
    }


    public void loadModelSprites(Level level) {

        List<LevelViewElement> observers = new ArrayList<>();

        observers.addAll(registerTerrainObservers(level.getTerrainMap()));

        observers.addAll(registerItemObservers(level.getItemMap()));

        observers.addAll(registerObstacleObservers(level.getObstacleMap()));

        observers.addAll(registerEntityObservers(level.getEntityMap()));

        observers.addAll(registerAreaEffectObservers(level.getAreaEffectMap()));

        observers.addAll(registerTrapObservers(level.getTrapMap()));

        observers.addAll(registerRiverObservers(level.getRiverMap()));

        observers.addAll(registerMountObservers(level.getMountMap()));

        observers.addAll(registerInfluenceEffectObservers(level.getInfluenceEffectMap()));

        observers.addAll(registerDecalObservers(level.getDecalMap()));

        level.setObservers(observers);
    }

    private ArrayList<LevelViewElement> registerDecalObservers(Map<Point3D, Decal> decalMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : decalMap.keySet()) {
            //   Decal decal = decalLocations.get(point); TODO: needed?

            DecalView observer = new DecalView(point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerInfluenceEffectObservers(Map<Point3D, InfluenceEffect> influenceEffectMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : influenceEffectMap.keySet()) {
            //   InfluenceEffect effect = influenceEffectLocations.get(point); TODO: needed?

            InfluenceEffectView observer = new InfluenceEffectView(point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerMountObservers(Map<Point3D, Mount> mountMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : mountMap.keySet()) {
            Mount mount = mountMap.get(point);

            MountView observer = new MountView(mount, point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerRiverObservers(Map<Point3D, River> riverMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : riverMap.keySet()) {
            River river = riverMap.get(point);

            RiverView observer = new RiverView(river, point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerTrapObservers(Map<Point3D, Trap> trapMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : trapMap.keySet()) {
            Trap trap = trapMap.get(point);

            TrapView observer = new TrapView(trap, point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerAreaEffectObservers(Map<Point3D, AreaEffect> areaEffectMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : areaEffectMap.keySet()) {
            AreaEffectView observer = new AreaEffectView(point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerEntityObservers(BidiMap<Point3D, Entity> entityMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : entityMap.getKeyList()) {
            Entity entity = entityMap.getValueFromKey(point);

            LevelViewElement observer = entity.getObserver();
            observer.setPosition(point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerObstacleObservers(Map<Point3D, Obstacle> obstacleMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : obstacleMap.keySet()) {
            ObstacleView observer = new ObstacleView(point);

            observers.add(observer);
        }

        return observers;

    }

    private ArrayList<LevelViewElement> registerItemObservers(Map<Point3D, Item> itemMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : itemMap.keySet()) {
            ItemView observer = new ItemView(point);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerTerrainObservers(Map<Point3D, Terrain> terrainMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : terrainMap.keySet()) {
            Terrain terrain = terrainMap.get(point);

            TerrainView observer = new TerrainView(terrain, point);

            observers.add(observer);
            terrainObservers.add(observer);
        }

        return observers;
    }

    public void setHotBarView(HotbarView hbv) { levelView.setHotbarView(hbv);}

    public void closeMenu() {
        menuView.setInMenu(false);
    }

    public void updateTerrainFog(Point3D playerPos, int playerSightRadius) {
        HexMathHelper hexMathHelper = new HexMathHelper();
        for(TerrainView o: terrainObservers) {
            if(hexMathHelper.getDistance(playerPos, o.getLocation()) <= playerSightRadius) {
                o.setShrouded(false);
            } else {
                o.setShrouded(true);
            }
        }
    }
}
