package Model.Command.GameModelCommand;

import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class TeleportEntityCommand extends GameModelCommand {

    private Level destinationLevel;
    private Point3D destinationPoint;

    public TeleportEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void receiveGameModel(GameModel gameModel) {

    }

    @Override
    public void execute(Entity entity) {

    }
}
