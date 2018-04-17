package Model.Command.EntityCommand.NonSettableCommand;

import Model.Command.Command;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class TeleportEntityCommand extends GameModelCommand implements Command {

    private Level destinationLevel;
    private Point3D destinationPoint;
    private Entity entity;

    public TeleportEntityCommand(LevelMessenger levelMessenger, Level destinationLevel, Point3D destinationPoint) {
        super(levelMessenger);
        this.destinationLevel = destinationLevel;
        this.destinationPoint = destinationPoint;
    }

    public Level getDestinationLevel() {
        return destinationLevel;
    }

    public Point3D getDestinationPoint() {
        return destinationPoint;
    }

    public Entity getEntity() {
        return entity;
    }

    public void receiveGameModel(GameModel gameModel) {
        gameModel.addToTeleportQueue(this);
    }

    public void receiveLevel(Level level) {
        level.removeEntityFrom(entity);
    }

    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel();
    }
}
