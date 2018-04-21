package Model.Command.EntityCommand.NonSettableCommand;

import Controller.GameLoop;
import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class DialogCommand extends GameLoopCommand implements Command {

    private Entity invokingEntity;
    private Entity receivingEntity;
    private Point3D receiverPoint;

    public DialogCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void receiveGameLoop(GameLoop gameLoop) {
        if(invokingEntity != null && receivingEntity != null) {
            gameLoop.openDialogWindow(invokingEntity, receivingEntity);
        }
    }

    public void receiveGameModel(GameModel gameModel) {
        if(!gameModel.entityIsPlayer(invokingEntity)) {
            invokingEntity = null;
        }
    }

    public void receiveLevel(Level level) {
        Point3D invokerPoint = level.getEntityPoint(invokingEntity);

        receiverPoint = Orientation.getAdjacentPoint(invokerPoint, invokingEntity.getOrientation());

        receivingEntity = level.getEntityAtPoint(receiverPoint);
    }

    public void execute(Entity entity) {
        this.invokingEntity = entity;
        sendCommandToGameLoop();
    }

    public void accept(Visitor visitor) {
        visitor.visitDialogCommand(this);
    }
}
