package Model.Level;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.AI.AIController;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Entity.Entity;
import View.LevelView.EntityView;
import View.LevelView.RiverView;
import View.LevelView.TerrainView;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import java.io.IOException;
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

    public GameModel() {
            levels = new ArrayList<>();
            aiMap = new HashMap<>();
            teleportQueue = new LinkedList<>();


            currentLevel = new Level(new ArrayList<>());
            player = new Entity();
            currentLevel.addEntityTo(new Point3D(0, 0, 0), player);
            //currentLevel.addObstacleTo(new Point3D(-1, 0, 1), new Obstacle());


            currentLevel.addTerrainTo(new Point3D(-1, 0, 1), Terrain.GRASS);
            currentLevel.addTerrainTo(new Point3D(0, 0, 0), Terrain.GRASS);
            currentLevel.addTerrainTo(new Point3D(1, 0, -1), Terrain.GRASS);
            currentLevel.addRiverTo(new Point3D(0, -1, 1), new River(new Vec3d(0, -1, 1)));
            //currentLevel.addMountTo(new Point3D(1, 0, -1), new Mount());

    }

    public GameModel(Level currentLevel, LevelMessenger currentLevelMessenger, List<Level> levels, Entity player,
                     Map<Level, List<AIController>> aiMap) {
        this.currentLevel = currentLevel;
        this.currentLevelMessenger = currentLevelMessenger;
        this.levels = levels;
        this.player = player;
        this.aiMap = aiMap;
    }

    public Level getCurrentLevel(){
        return currentLevel;
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

    @Override
    public void accept(SavingVisitor visitor) {
        try {
            if(currentLevel != null) {
                visitor.saveCurrentLevel(currentLevel);
            }

            if(!levels.isEmpty()) {
                visitor.saveLevelList(levels);
            }

            if(player != null) {
                visitor.visitEntity(player);
            }
        }

        catch (IOException e) {
            e.printStackTrace();
        }
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
    }

    private void changeLevels(Entity entity, Level destinationLevel, Point3D destinationPoint) {

        destinationLevel.addEntityTo(destinationPoint, entity);

        System.out.println("hi"+destinationPoint.toString());

        if(entity.equals(player)) {
            currentLevel = destinationLevel;
            currentLevelMessenger.setLevel(currentLevel);
            // TODO: notify pets when player teleports, so we can teleport them as well
        }
    }

    public void advance() {

        currentLevel.processMoves();
        currentLevel.processInteractions();

        processTeleportQueue();
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
    }

    public void setGameModelMessenger(GameModelMessenger gameModelMessenger) {
        this.gameModelMessenger = gameModelMessenger;
    }
}
