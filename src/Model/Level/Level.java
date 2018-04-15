package Model.Level;

import Model.AreaEffect.AreaEffect;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level {

    private Map<Point3D, Terrain> terrainLocations;
    private Map<Point3D, Item> itemLocations;
    private Map<Point3D, Obstacle> obstacleLocations;
    private Map<Point3D, Entity> entityLocations;
    private Map<Point3D, AreaEffect> areaEffectLocations;
    private Map<Point3D, Trap> trapLocations;
    private Map<Point3D, River> riverLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;
    private Map<Point3D, Decal> decalLocations;

    private List<LevelViewElement> observers;

    private MovementHandler movementHandler;
    private InteractionHandler interactionHandler;

    private List<Point3D> tilesSeenByPlayer;

    public Level(List<LevelViewElement> observers) {
        this.terrainLocations = new HashMap<>();
        this.itemLocations = new HashMap<>();
        this.obstacleLocations = new HashMap<>();
        this.entityLocations = new HashMap<>();
        this.areaEffectLocations = new HashMap<>();
        this.trapLocations = new HashMap<>();
        this.riverLocations = new HashMap<>();
        this.mountLocations = new HashMap<>();
        this.influenceEffectLocations = new HashMap<>();
        this.decalLocations = new HashMap<>();

        this.observers = observers;

        this.movementHandler = new MovementHandler(terrainLocations,obstacleLocations,entityLocations,mountLocations);

        this.interactionHandler = new InteractionHandler(itemLocations, entityLocations, areaEffectLocations,
                                                         trapLocations, mountLocations, influenceEffectLocations,
                riverLocations, observers);

        this.tilesSeenByPlayer = new ArrayList<>();
    }

    // TODO: process moves using movementHandler

    public void processInteractions() {
        interactionHandler.processInteractions();
    }

    public void addTerrainTo(Point3D point, Terrain terrain) {
        terrainLocations.put(point, terrain);
    }

    public void addItemnTo(Point3D point, Item item) {
        itemLocations.put(point, item);
    }

    public void addObstacleTo(Point3D point, Obstacle obstacle) {
        obstacleLocations.put(point, obstacle);
    }

    public void addEntityTo(Point3D point, Entity entity) {
        entityLocations.put(point, entity);
    }

    public void removeEntityFrom(Entity entity){
        if(entityLocations.containsValue(entity)) {
            for(Point3D point: entityLocations.keySet()) {
                if(entityLocations.get(point) == entity) {
                    entityLocations.remove(point);
                }
            }
        }
    }

    public void addAreaEffectTo(Point3D point, AreaEffect areaEffect) {
        areaEffectLocations.put(point, areaEffect);
    }

    public void addTrapTo(Point3D point, Trap trap) {
        trapLocations.put(point, trap);
    }

    public void addRiverTo(Point3D point, River river) {
        riverLocations.put(point, river);
    }

    public void addMountTo(Point3D point, Mount mount) {
        mountLocations.put(point, mount);
    }

    public void addInfluenceEffectTo(Point3D point, InfluenceEffect influenceEffect) {
        influenceEffectLocations.put(point, influenceEffect);
    }

    public void addDecalTo(Point3D point, Decal decal) {
        decalLocations.put(point, decal);
    }

    public Point3D getEntityPoint(Entity entity) {
        if(entityLocations.containsValue(entity)) {
            for(Point3D point: entityLocations.keySet()) {
                if(entityLocations.get(point) == entity) {
                    return point;
                }
            }
        }

        return null;
    }

    public Map<Point3D, InfluenceEffect> getInfluencesMap() {
        return influenceEffectLocations;
    }
    
    public boolean hasItem(Item item) {
        return itemLocations.containsValue(item);
    }

    public void processMoves() {
        movementHandler.processMoves();
    }

    public Map<Point3D, Terrain> getTerrainMap() {
        return terrainLocations;
    }
}
