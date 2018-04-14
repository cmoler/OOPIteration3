package Model.Command.GameModelCommand;

import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class TeleportEntityCommand extends GameModelCommand {

    private Level sourceLevel;
    private Level destinationLevel;
    private Point3D destinationPoint;
    private Entity entity;

    public TeleportEntityCommand(LevelMessenger levelMessenger, Level sourceLevel, Level destinationLevel, Point3D destinationPoint) {
        super(levelMessenger);
        this.sourceLevel = sourceLevel;
        this.destinationLevel = destinationLevel;
        this.destinationPoint = destinationPoint;
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {
        gameModel.moveEntity(entity, sourceLevel, destinationLevel, destinationPoint);
    }

    @Override
    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToGameModel(this);
    }
}
