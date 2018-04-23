package View;


import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.Item.Item;
import Model.Level.*;
import Model.Utility.BidiMap;
import View.LevelView.*;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Renderer {

    private LevelView levelView;

    private MenuView menuView;

    Map<Level, List<TerrainView>> terrainObserversMap;

    public Renderer() {
        levelView = new LevelView();

        menuView = new MenuView();

        terrainObserversMap = new HashMap<>();

        Sprites.getInstance().initSprites();
    }

    public void render(GraphicsContext gc, Point3D playerPos, Point2D scrollOffset) {
        if(menuView.inMenu()) {
            menuView.render(gc);
        } else {
         //   levelView.refreshInfluenceEffectSprites();
            levelView.render(gc, playerPos, scrollOffset);
        }
    }

    public void renderMenu(GraphicsContext gc) {
        if(menuView.inMenu()) {
            menuView.render(gc);
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
        if (level != null) {
            observers.addAll(registerTerrainObservers(level, level.getTerrainMap()));

            observers.addAll(registerItemObservers(level.getItemMap()));

            observers.addAll(registerObstacleObservers(level.getObstacleMap()));

            observers.addAll(registerEntityObservers(level.getEntityMap()));

            observers.addAll(registerAreaEffectObservers(level.getAreaEffectMap()));

            observers.addAll(registerTrapObservers(level.getTrapMap()));

            observers.addAll(registerRiverObservers(level.getRiverMap()));

            observers.addAll(registerMountObservers(level.getMountMap()));

            //observers.addAll(registerInfluenceEffectObservers(level.getInfluenceEffectMap()));

            observers.addAll(registerDecalObservers(level.getDecalMap()));

            level.setObservers(observers);
        }
    }

    private ArrayList<LevelViewElement> registerDecalObservers(Map<Point3D, Decal> decalMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : decalMap.keySet()) {

            DecalView observer = new DecalView(point);

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
            trap.setObserver(observer);

            observers.add(observer);
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerAreaEffectObservers(Map<Point3D, AreaEffect> areaEffectMap) {
        ArrayList<LevelViewElement> observers = new ArrayList<>();

        for(Point3D point : areaEffectMap.keySet()) {
            AreaEffect areaEffect = areaEffectMap.get(point);
            observers.add(areaEffect.getObserver());
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

            Item item = itemMap.get(point);

            observers.add(item.getObserver());
        }

        return observers;
    }

    private ArrayList<LevelViewElement> registerTerrainObservers(Level level, Map<Point3D, Terrain> terrainMap) {
        ArrayList<TerrainView> observers = new ArrayList<>();

        for(Point3D point : terrainMap.keySet()) {
            Terrain terrain = terrainMap.get(point);

            TerrainView observer = new TerrainView(terrain, point);

            observers.add(observer);

        }

        terrainObserversMap.put(level, observers);

        return new ArrayList<>(observers);
    }

    public void setHotBarView(HotbarView hbv) { levelView.setHotbarView(hbv);}

    public void closeMenu() {
        menuView.setInMenu(false);
    }

    public void updateTerrainFog(Level currentLevel, Point3D playerPos, int playerSightRadius) {
        if(!menuView.inMenu()) {
            HexMathHelper hexMathHelper = new HexMathHelper();
            for (TerrainView terrainView : terrainObserversMap.get(currentLevel)) {
                if (hexMathHelper.getDistance(playerPos, terrainView.getLocation()) <= playerSightRadius) {
                    terrainView.setShrouded(false);
                } else {
                    terrainView.setShrouded(true);
                }
            }
        }
    }

    public void addObservationView(ObservationView observationView) {
        levelView.addObservationView(observationView);
    }
}
