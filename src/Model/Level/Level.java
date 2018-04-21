package Model.Level;

import Model.AreaEffect.AreaEffect;
import Model.Command.Command;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.Item;
import Model.Utility.BidiMap;
import Model.Item.TakeableItem.TakeableItem;
import View.LevelView.*;
import javafx.geometry.Point3D;

import java.util.*;

public class Level {

    private Map<Point3D, Terrain> terrainLocations;
    private Map<Point3D, Item> itemLocations;
    private Map<Point3D, Obstacle> obstacleLocations;
    private BidiMap<Point3D, Entity> entityLocations;
    private Map<Point3D, AreaEffect> areaEffectLocations;
    private Map<Point3D, Trap> trapLocations;
    private Map<Point3D, River> riverLocations;
    private Map<Point3D, Mount> mountLocations;
    private Map<Point3D, InfluenceEffect> influenceEffectLocations;
    private Map<Point3D, Decal> decalLocations;

    private List<LevelViewElement> observers;
    private List<TerrainView> terrainObservers;

    private MovementHandler movementHandler;
    private InteractionHandler interactionHandler;

    private List<Point3D> tilesSeenByPlayer;

    public Level() {
        this.terrainLocations = new HashMap<>();
        this.itemLocations = new HashMap<>();
        this.obstacleLocations = new HashMap<>();
        this.entityLocations = new BidiMap<>();
        this.areaEffectLocations = new HashMap<>();
        this.trapLocations = new HashMap<>();
        this.riverLocations = new HashMap<>();
        this.mountLocations = new HashMap<>();
        this.influenceEffectLocations = new HashMap<>();
        this.decalLocations = new HashMap<>();

        this.observers = new ArrayList<>();
        this.terrainObservers = new ArrayList<>();

        this.movementHandler = new MovementHandler(terrainLocations, obstacleLocations, entityLocations,
                                                   mountLocations, influenceEffectLocations);

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

    public BidiMap<Point3D, Entity> getEntityLocations() {
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
                !entityLocations.hasKey(point) &&
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
        if(entity.getObservers().size() == 0) {
            entity.addObserver(new EntityView(entity, point));
        }
        entityLocations.place(point, entity);
    }

    public void removeEntityFrom(Entity entity){
        entityLocations.removeByValue(entity);
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
        return entityLocations.getKeyFromValue(entity);
    }

    public List<LevelViewElement> getObservers() {
        return observers;
    }

    public void addObservers(LevelViewElement... newObserver) {
        if(Arrays.asList(newObserver).isEmpty()) {
            return;
        } else {
            observers.addAll(Arrays.asList(newObserver));
        }
    }

    public Entity getEntityAtPoint(Point3D point) {
        return entityLocations.getValueFromKey(point);
    }

    public boolean hasEntityAtPoint(Point3D point3D) {
        return getEntityAtPoint(point3D) != null;
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
        for(Point3D point : entityLocations.getKeyList()) {
            if(entityLocations.getValueFromKey(point).equals(entity)) {
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
            trapLocations.get(northPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
        }

        if(trapLocations.get(northeastPoint) != null) {
            trapLocations.get(northeastPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
        }

        if(trapLocations.get(northWestPoint) != null) {
            trapLocations.get(northWestPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
        }

        if(trapLocations.get(southPoint) != null) {
            trapLocations.get(southPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
        }

        if(trapLocations.get(southeastPoint) != null) {
            trapLocations.get(southeastPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
        }

        if(trapLocations.get(southwestPoint) != null) {
            trapLocations.get(southwestPoint).disarm(entityLocations.getValueFromKey(originPoint), disarmStrength);
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

    public void registerObservers() {
        registerTerrainObservers();

        registerItemObservers();

        registerObstacleObservers();

        registerEntityObservers();

        registerAreaEffectObservers();

        registerTrapObservers();

        registerRiverObservers();

        registerMountObservers();

        registerInfluenceEffectObservers();

        registerDecalObservers();
    }

    private void registerDecalObservers() {
        for(Point3D point : decalLocations.keySet()) {
         //   Decal decal = decalLocations.get(point); TODO: needed?

            DecalView observer = new DecalView(point);
            addObservers(observer);
        }
    }

    private void registerInfluenceEffectObservers() {
        for(Point3D point : influenceEffectLocations.keySet()) {
         //   InfluenceEffect effect = influenceEffectLocations.get(point); TODO: needed?

            InfluenceEffectView observer = new InfluenceEffectView(point);
            addObservers(observer);
        }
    }

    private void registerMountObservers() {
        for(Point3D point : mountLocations.keySet()) {
            Mount mount = mountLocations.get(point);

            MountView observer = new MountView(mount, point);
            addObservers(observer);
        }
    }

    private void registerRiverObservers() {
        for(Point3D point : riverLocations.keySet()) {
            River river = riverLocations.get(point);

            RiverView observer = new RiverView(river, point);
            addObservers(observer);
        }
    }

    private void registerTrapObservers() {
        for(Point3D point : trapLocations.keySet()) {
            Trap trap = trapLocations.get(point);

            TrapView observer = new TrapView(trap, point);
            addObservers(observer);
        }
    }

    private void registerAreaEffectObservers() {
        for(Point3D point : areaEffectLocations.keySet()) {
            AreaEffectView observer = new AreaEffectView(point);
            addObservers(observer);
        }
    }

    private void registerEntityObservers() {
        for(Point3D point : entityLocations.getKeyList()) {
            Entity entity = entityLocations.getValueFromKey(point);

            //EntityView observer = new EntityView(entity, point);
            if(entity.getObservers().size() < 1) { continue; }
            LevelViewElement observer = entity.getObservers().get(0);

            addObservers(observer);
        }
    }

    private void registerObstacleObservers() {
        for(Point3D point : obstacleLocations.keySet()) {
            ObstacleView observer = new ObstacleView(point);
            addObservers(observer);
        }
    }

    private void registerItemObservers() {
        for(Point3D point : itemLocations.keySet()) {
            ItemView observer = new ItemView(point);
            addObservers(observer);
        }
    }

    private void registerTerrainObservers() {
        for(Point3D point : terrainLocations.keySet()) {
            Terrain terrain = terrainLocations.get(point);

            TerrainView observer = new TerrainView(terrain, point);
            addObservers(observer);
            terrainObservers.add(observer);

        }
    }

    public void updateTerrainFog(Point3D playerPos, int playerViewDistance) {
        HexMathHelper hexMathHelper = new HexMathHelper();
        if(terrainObservers == null) { return; }

        for(TerrainView o: terrainObservers) {
            if(hexMathHelper.getDistance(playerPos, o.getLocation()) <= playerViewDistance) {
                o.setShrouded(false);
            } else {
                o.setShrouded(true);
            }
        }
    }

    public void clearObservers() {
        observers.clear();
    }

    public void registerEntityObserver(Entity entity) {
        Point3D point = getEntityPoint(entity);

        List<LevelViewElement> observers = getObservers();

        addEntityObservers(observers);
    }

    private void addEntityObservers(List<LevelViewElement> observers) {
        addObservers(observers.toArray(new LevelViewElement[observers.size()]));
    }

    public void deregisterEntityObserver(Entity entity) {
        // find match in entity and level observers lists
        // add match to list of observers to remove
        // remove from level observers and entity observers
        List<LevelViewElement> entityObservers = entity.getObservers();

        List<LevelViewElement> observersToRemove = new ArrayList<>();

        for(LevelViewElement entityObserver : entityObservers) {
            if(observers.contains(entityObserver)) {
                observersToRemove.add(entityObserver);

                entity.removeObserver(entityObserver);
            }
        }

        observers.removeAll(observersToRemove);
    }

    public void setMovementHandlerDialogCommand(LevelMessenger levelMessenger) {
        movementHandler.setDialogCommandLevelMessenger(levelMessenger);
    }
}