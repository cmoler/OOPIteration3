package Model.Level;

import Model.AreaEffect.AreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.ObserveEntityCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import Model.Item.TakeableItem.TakeableItem;
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

        this.movementHandler = new MovementHandler(terrainLocations,obstacleLocations,entityLocations,mountLocations, influenceEffectLocations);

        this.interactionHandler = new InteractionHandler(itemLocations, entityLocations, areaEffectLocations,
                                                         trapLocations, mountLocations, influenceEffectLocations,
                riverLocations, observers);

        this.tilesSeenByPlayer = new ArrayList<>();


    }

    public Map<Point3D, Terrain> getTerrainLocations() {
        return terrainLocations;
    }

    public Map<Point3D, Item> getItemLocations() {
        return itemLocations;
    }

    public Map<Point3D, Obstacle> getObstacleLocations() {
        return obstacleLocations;
    }

    public Map<Point3D, Entity> getEntityLocations() {
        return entityLocations;
    }

    public Map<Point3D, AreaEffect> getAreaEffectLocations() {
        return areaEffectLocations;
    }

    public Map<Point3D, Trap> getTrapLocations() {
        return trapLocations;
    }

    public Map<Point3D, River> getRiverLocations() {
        return riverLocations;
    }

    public Map<Point3D, Mount> getMountLocations() {
        return mountLocations;
    }

    public Map<Point3D, InfluenceEffect> getInfluenceEffectLocations() {
        return influenceEffectLocations;
    }

    public Map<Point3D, Decal> getDecalLocations() {
        return decalLocations;
    }

    public void processInteractions() {
        interactionHandler.processInteractions();
    }

    public void addItemnTo(Point3D point, Item item) {
        itemLocations.put(point, item);
    }

    public void dropItemFromEntity(Entity entity, TakeableItem item) {
        Point3D entityPoint = getEntityPoint(entity);

        List<Point3D> candidatePoints = Orientation.getAllAdjacentPoints(entityPoint);

        for(Point3D point : candidatePoints) {
            if(canDropItemAtPoint(point)) {
                dropItemAtPoint(point, item);
                entity.removeItemFromInventory(item);

                break;
            }
        }
    }

    private boolean canDropItemAtPoint(Point3D point) {
        // get nearest point that does not have (1) another item, (2) an entity, (3) a mount, (4) obstacle, (5) no terrain
        return  !itemLocations.containsKey(point) &&
                !entityLocations.containsKey(point) &&
                !mountLocations.containsKey(point) &&
                !obstacleLocations.containsKey(point) &&
                terrainLocations.containsKey(point);
    }

    private void dropItemAtPoint(Point3D point3D, Item item) {
        item.clearDeletionFlag();
        itemLocations.put(point3D, item);
    }

    public void addTerrainTo(Point3D point, Terrain terrain) {
        terrainLocations.put(point, terrain);
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
                if(entityLocations.get(point).equals(entity)) {
                    return point;
                }
            }
        }

        return null;
    }

    public List<LevelViewElement> getObservers() {
        return observers;
    }
    public void addObserver(LevelViewElement newObserver) {
        if(newObserver == null) { return; }
        observers.add(newObserver);
    }

    public Entity getEntityAtPoint(Point3D point) {
        if (entityLocations.containsKey(point)) {
            return entityLocations.get(point);
        } else {
            return null;
        }
    }

    public Point3D getItemPoint(Item item) {
        if(itemLocations.containsValue(item)) {
            for(Point3D point: itemLocations.keySet()) {
                if(itemLocations.get(point).equals(item)) {
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

    public void disarmTrapFromEntity(Entity entity, int disarmStrength) {
        for(Point3D point : entityLocations.keySet()) {
            if(entityLocations.get(point).equals(entity)) {
                disarmTrapsAtPoint(point, disarmStrength);
            }
        }
    }

    private void disarmTrapsAtPoint(Point3D originPoint, int disarmStrength) {
        Point3D northPoint = Orientation.getAdjacentPoint(originPoint, Orientation.NORTH);
        Point3D northeastPoint = Orientation.getAdjacentPoint(originPoint, Orientation.NORTHEAST);
        Point3D northWestPoint = Orientation.getAdjacentPoint(originPoint, Orientation.NORTHWEST);
        Point3D southPoint = Orientation.getAdjacentPoint(originPoint, Orientation.SOUTH);
        Point3D southeastPoint = Orientation.getAdjacentPoint(originPoint, Orientation.SOUTHEAST);
        Point3D southwestPoint = Orientation.getAdjacentPoint(originPoint, Orientation.SOUTHWEST);

        if(trapLocations.get(northPoint) != null) {
            trapLocations.get(northPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }

        if(trapLocations.get(northeastPoint) != null) {
            trapLocations.get(northeastPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }

        if(trapLocations.get(northWestPoint) != null) {
            trapLocations.get(northWestPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }

        if(trapLocations.get(southPoint) != null) {
            trapLocations.get(southPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }

        if(trapLocations.get(southeastPoint) != null) {
            trapLocations.get(southeastPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }

        if(trapLocations.get(southwestPoint) != null) {
            trapLocations.get(southwestPoint).disarm(entityLocations.get(originPoint), disarmStrength);
        }
    }

    public void removeInfluenceEffectsWithCommand(Command command) {
        List<Point3D> influenceEffectPoints = new ArrayList<>(influenceEffectLocations.keySet());

        for(Point3D point : influenceEffectPoints) {
            InfluenceEffect influenceEffect = influenceEffectLocations.get(point);

            if(influenceEffect.getCommand().equals(command)) { // remove all instances of the influence effect if the command matches
                influenceEffectLocations.remove(point, influenceEffect);
            }
        }
    }
}