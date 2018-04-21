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
    private List<TerrainView> tilesSeenByPlayer;

    private MovementHandler movementHandler;
    private InteractionHandler interactionHandler;

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
        this.tilesSeenByPlayer = new ArrayList<>();

        this.movementHandler = new MovementHandler(terrainLocations, obstacleLocations, entityLocations,
                mountLocations, influenceEffectLocations);

        this.interactionHandler = new InteractionHandler(itemLocations, entityLocations, areaEffectLocations,
                trapLocations, mountLocations, influenceEffectLocations,
                riverLocations, observers);

        this.tilesSeenByPlayer = new ArrayList<>();
    }

    public Map<Point3D, Terrain> getTerrainMap() {
        return terrainLocations;
    }

    public Map<Point3D, Item> getItemMap() {
        return itemLocations;
    }

    public Map<Point3D, Obstacle> getObstacleMap() {
        return obstacleLocations;
    }

    public BidiMap<Point3D, Entity> getEntityMap() {
        return entityLocations;
    }

    public Map<Point3D, AreaEffect> getAreaEffectMap() {
        return areaEffectLocations;
    }

    public Map<Point3D, Trap> getTrapMap() {
        return trapLocations;
    }

    public Map<Point3D, River> getRiverMap() {
        return riverLocations;
    }

    public Map<Point3D, Mount> getMountMap() {
        return mountLocations;
    }

    public Map<Point3D, InfluenceEffect> getInfluenceEffectMap() {
        return influenceEffectLocations;
    }

    public Map<Point3D, Decal> getDecalMap() {
        return decalLocations;
    }

    public void advance() {
        processMoves();
        processInteractions();
        regenEntitiesMana();
    }

    private void processMoves() {
        movementHandler.processMoves();
    }

    private void processInteractions() {
        interactionHandler.processInteractions();
    }

    private void regenEntitiesMana() {
        for(Entity entity : entityLocations.getValueList()) {
            entity.regenerateMana();
        }
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
        System.out.println(point);
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

    public void setObservers(List<LevelViewElement> observers) {
        this.observers = observers;
    }

    public void setTilesSeenByPlayer(List<TerrainView> tilesSeenByPlayer) {
        this.tilesSeenByPlayer = tilesSeenByPlayer;
    }

    public Map<Point3D, InfluenceEffect> getInfluencesMap() {
        return influenceEffectLocations;
    }

    public boolean hasItem(Item item) {
        return itemLocations.containsValue(item);
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

    public void updateRenderLocations(Point3D playerPos, int playerViewDistance) {
        HexMathHelper hexMathHelper = new HexMathHelper();

        for(LevelViewElement o:observers) {
            if(hexMathHelper.getDistance(playerPos, o.getLocation()) <= playerViewDistance) {//If object in view of player, update location
                o.locationViewedByPlayer();
            }
            if(hexMathHelper.getDistance(playerPos, o.getRenderLocation()) <= playerViewDistance) {
                o.rendererLocationViewedByPlayer();
            }
        }
    }
    public void setMovementHandlerDialogCommand(LevelMessenger levelMessenger) {
        movementHandler.setDialogCommandLevelMessenger(levelMessenger);
    }

    public void addObservers(List<LevelViewElement> observers) {
        observers.addAll(observers);
    }

    public void removeObservers(List<LevelViewElement> observers) {
        observers.removeAll(observers);
    }

    public List<LevelViewElement> getObservers() {
        return observers;
    }

    public Point3D findNearestOpenPointForEntity(Point3D destinationPoint) {

        Queue<Point3D> pointsToProcess = new LinkedList<>();
        List<Point3D> pointsProcessed = new ArrayList<>();

        pointsToProcess.add(destinationPoint);

        while(!pointsToProcess.isEmpty()) {
            Point3D point = ((LinkedList<Point3D>) pointsToProcess).getFirst();

            System.out.println("PROCESSING "+point.toString());

            for(Orientation orientation : Orientation.values()) {
                if(canPlaceEntityAtPoint(Orientation.getAdjacentPoint(point, orientation), entityLocations.getValueFromKey(destinationPoint))) {
                    return Orientation.getAdjacentPoint(point, orientation);
                } else {
                    if (!pointsProcessed.contains(point)) {
                        pointsToProcess.add(Orientation.getAdjacentPoint(point, orientation));
                    }
                }
            }

            pointsProcessed.add(point);
            pointsToProcess.remove(point);
        }

        return null;
    }

    private boolean canPlaceEntityAtPoint(Point3D point3D, Entity entityToPlace) {
        return !entityLocations.hasKey(point3D) && terrainLocations.containsKey(point3D)
                && !obstacleLocations.containsKey(point3D) && entityToPlace.canMoveOnTerrain(terrainLocations.get(point3D));
    }

    public void moveEntityFromFirstPointToSecondPoint(Point3D destinationPoint, Point3D pointToAddTo, Entity entityAtPoint) {
        entityLocations.removeByKey(destinationPoint);
        entityLocations.place(pointToAddTo, entityAtPoint);
    }

    public ArrayList<Entity> clearDeadEntities(Entity player) {
        ArrayList<Entity> deadpool = new ArrayList<>();
        for(Entity ent: entityLocations.getValueList()){
            if (ent.isDead() && !ent.equals(player)){
                ent.notifyUponDeath();
                ent.setMoveable(false);
                deadpool.add(ent);
            }
        }
        return deadpool;
    }
}