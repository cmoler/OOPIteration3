package Model.Level;

import Model.AI.AIController;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Command.EntityCommand.NonSettableCommand.TeleportEntityCommand;
import Model.Entity.Entity;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GameModel {

    private Level currentLevel;
    private LevelMessenger currentLevelMessenger;
    private List<Level> levels;
    private Entity player;
    private Map<Level, List<AIController>> aiMap;
    private Queue<TeleportTuple> teleportTupleQueue;

    public GameModel() {

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

        teleportTupleQueue.add(tuple);
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
        for(TeleportTuple tuple : teleportTupleQueue) {
            changeLevels(tuple.getEntity(), tuple.getDestLevel(), tuple.getDestinationPoint());
        }

        teleportTupleQueue.clear();
    }

    private void changeLevels(Entity entity, Level destinationLevel, Point3D destinationPoint) {

        destinationLevel.addEntityTo(destinationPoint, entity);

        if(entity.equals(player)) {
            currentLevel = destinationLevel;
            currentLevelMessenger.setLevel(currentLevel);
            // TODO: notify pets when player teleports, so we can teleport them as well
        }
    }

    public void advance() {
        currentLevel.processMoves();
        currentLevel.processInteractions();
    }
}
