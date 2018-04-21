package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.AI.PetAI.PetStates.PassivePetState;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.SightRadius;
import Model.InfluenceEffect.RadialInfluenceEffect;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.*;

public class GameModel implements Visitable {

    private GameModelMessenger gameModelMessenger;

    private List<Level> levels;
    private Level currentLevel;
    private LevelMessenger currentLevelMessenger;

    private Entity player;

    private Map<Level, List<AIController>> aiMap;

    private Queue<TeleportTuple> teleportQueue;
    private Queue<TeleportTuple> failedTeleportQueue;

    public GameModel(GameLoopMessenger gameLoopMessenger) {
        gameModelMessenger = new GameModelMessenger(gameLoopMessenger, this);
        currentLevel = new Level();
        currentLevelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);
        currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);

        levels = new ArrayList<>();
        levels.add(currentLevel);

        aiMap = new HashMap<>();
        teleportQueue = new LinkedList<>();
        failedTeleportQueue = new LinkedList<>();

        player = new Entity();

        player.setMoveable(true);
        player.setNoise(5);
        player.setSightRadius(new SightRadius(7));
        currentLevel.addEntityTo(new Point3D(0, -5, 5), player);

        RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(new RemoveHealthCommand(15), 10, 5, Orientation.SOUTHEAST);

        for(int i = 0; i < 8; i++) {
            ArrayList<Point3D> points = radialInfluenceEffect.nextMove(new Point3D(0, 0, 0));
            for(int j = 0; j < points.size(); j++) {
                currentLevel.addTerrainTo(points.get(j), Terrain.GRASS);
            }
        }

        currentLevel.addRiverTo(new Point3D(1, 0, -1), new River(new Vec3d(0, 1, -1)));

        //currentLevel.addMountTo(new Point3D(0, 1, -1), new Mount());

        Entity enemy =  new Entity();
        enemy.setMoveable(true);
        enemy.setNoise(5);
        enemy.setSightRadius(new SightRadius(2));
        ArrayList<Vec3d> path = new ArrayList<>();
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(1,0,-1));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(-1,1,0));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(0,-1,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(-1,0,1));
        path.add(new Vec3d(1,-1,0));
        path.add(new Vec3d(1,-1,0));//*/
        currentLevel.addEntityTo(new Point3D(0, 3, -3),enemy);
        List<Entity> list = new ArrayList<>();
        list.add(player);
        HostileAI hostileAI = new HostileAI(enemy,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(),list);
        //hostileAI.setPatrolPath(new PatrolPath(path));
        AIController controller = new AIController();
        controller.setActiveState(hostileAI);
        List<AIController> AIList = new ArrayList<>();
        AIList.add(controller);
        aiMap.put(currentLevel,AIList);

        Entity pet = new Entity();
        pet.setMoveable(true);
        pet.setNoise(5);
        pet.setSightRadius(new SightRadius(2));
        currentLevel.addEntityTo(new Point3D(5, -5, 0), pet);
        PassivePetState PPS = new PassivePetState(pet,currentLevel.getTerrainMap(),currentLevel.getEntityMap(),currentLevel.getObstacleMap(),player);
        AIController test = new AIController();
        test.setActiveState(PPS);
        AIList.add(test);
        aiMap.put(currentLevel,AIList);

        levels.add(currentLevel);
    }

    public GameModel(Level currentLevel, LevelMessenger currentLevelMessenger, List<Level> levels, Entity player,
                     Map<Level, List<AIController>> aiMap) {
        this.currentLevel = currentLevel;
        this.currentLevelMessenger = currentLevelMessenger;
        this.levels = levels;
        this.player = player;
        this.aiMap = aiMap;

        this.currentLevel.setMovementHandlerDialogCommand(this.currentLevelMessenger);
    }

    public Level getCurrentLevel(){
        return currentLevel;
    }

    public List<Level> getLevels() {
        return levels;
    }

    public AIController getAIForEntity(Entity entity) {
        ArrayList<AIController> ais = (ArrayList)aiMap.get(currentLevel);
        for(int i = 0; i < ais.size(); ++i){
            if(ais.get(i).getEntity() == entity){
                return ais.get(i);
            }
        }
        try {
            throw new Exception("couldnt find ai for that entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addToTeleportQueue(TeleportEntityCommand teleportEntityCommand) {
        TeleportTuple tuple = new TeleportTuple(teleportEntityCommand.getEntity(),
                                                teleportEntityCommand.getDestinationLevel(), teleportEntityCommand.getDestinationPoint());

        teleportQueue.add(tuple);
    }

    public boolean entityIsPlayer(Entity entity) {
        return entity.equals(player);
    }

    public boolean hasLevels() {
        return !levels.isEmpty();
    }

    public boolean hasPlayer() {
        return player != null;
    }

    public boolean hasCurrentLevel() {
        return currentLevel != null;
    }

    private class TeleportTuple {
        private Entity entity;
        private Level destLevel;
        private Point3D destinationPoint;

        public TeleportTuple(Entity entity, Level destLevel, Point3D destinationPoint) {
            this.entity = entity;
            this.destLevel = destLevel;
            this.destinationPoint = destinationPoint;
        }

        public Entity getEntity() {
            return entity;
        }

        public Level getDestLevel() {
            return destLevel;
        }

        public Point3D getDestinationPoint() {
            return destinationPoint;
        }
    }

    public void processTeleportQueue() {
        for(TeleportTuple tuple : teleportQueue) {
            changeLevels(tuple.getEntity(), tuple.getDestLevel(), tuple.getDestinationPoint());
        }

        teleportQueue.clear();

        teleportQueue.addAll(failedTeleportQueue);
    }

    private void changeLevels(Entity entity, Level destinationLevel, Point3D destinationPoint) {
        if(!destinationLevel.hasEntityAtPoint(destinationPoint)) {
            destinationLevel.addEntityTo(destinationPoint, entity);
            destinationLevel.addObservers(entity.getObservers());

            currentLevel.removeEntityFrom(entity);
            currentLevel.removeObservers(entity.getObservers());

            if (entity.equals(player)) {
                currentLevel = destinationLevel;
                currentLevelMessenger.setLevel(currentLevel);
                currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);
                // TODO: notify pets when player teleports, so we can teleport them as well
                // TODO: change logic for teleporting on a tile that is occupied by another entity
            }
        } else {
            failedTeleportQueue.add(new TeleportTuple(entity, destinationLevel, destinationPoint));
        }
    }

    public void advance() {
        processAIMoves();
        currentLevel.processMoves();
        currentLevel.processInteractions();
        currentLevel.updateTerrainFog(getPlayerPosition(), player.getSight());

        processTeleportQueue();
    }

    private void processAIMoves(){

        if(aiMap.containsKey(currentLevel)) {
            List<AIController> aiControllers = aiMap.get(currentLevel);

            for (AIController AI : aiControllers) {
                AI.processMove();
            }
        }
    }

    public boolean playerIsDead() {
        return player.isDead();
    }

    public Point3D getPlayerPosition() {
        return currentLevel.getEntityPoint(player);
    }

    public Entity getPlayer() {
        return player;
    }

    public void addLevel(Level level) {
        levels.add(level);
    }

    public void setCurrentLevel(Level level) {
        currentLevel = level;

        if(currentLevelMessenger != null) {
            currentLevelMessenger.setLevel(currentLevel);

        } else {
            if(gameModelMessenger == null) {
                throw new RuntimeException("GameModel's messenger not set!");
            }
            currentLevelMessenger = new LevelMessenger(gameModelMessenger, currentLevel);
        }

        currentLevel.setMovementHandlerDialogCommand(currentLevelMessenger);
    }

    public void accept(Visitor visitor) {
        visitor.visitGameModel(this);
    }
}
