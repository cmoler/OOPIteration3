package View;

import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import Model.Level.*;
import Model.Utility.BidiMap;
import View.LevelView.*;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import View.MenuView.TitleScreenView;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Renderer {

    private LevelView levelView;

    private MenuView menuView;

    public Renderer() {
        levelView = new LevelView();

        menuView = new MenuView();
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

    public void closeMenu() {
        menuView.setInMenu(false);
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

            EntityView observer = new EntityView(entity, point);

            observers.add(observer); // TODO: refactor logic for this method once we integrate factory functionality for entity sprites
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
        }

        return observers;
    }
}
