package Model.Command.EntityCommand.SettableCommand;

import Controller.GameLoop;
import Controller.Visitor.SavingVisitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class BarterCommand extends GameLoopCommand implements SettableCommand {

    private Entity invokingEntity;
    private Entity receivingEntity;

    private int playerBarterStrength;

    public BarterCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void receiveGameLoop(GameLoop gameLoop) {
        gameLoop.openBarterWindow(invokingEntity, playerBarterStrength, receivingEntity);
    }

    public void receiveGameModel(GameModel gameModel) {
        if(!gameModel.entityIsPlayer(invokingEntity)) {
            invokingEntity = null;
        }
    }

    public void receiveLevel(Level level) {
        Point3D invokerPoint = level.getEntityPoint(invokingEntity);

        Point3D receiverPoint = Orientation.getAdjacentPoint(invokerPoint, invokingEntity.getOrientation());

        receivingEntity = level.getEntityAtPoint(receiverPoint);
    }

    public void execute(Entity entity) {
        this.invokingEntity = entity;
        sendCommandToGameLoop();
    }

    public void setAmount(int amount) {
        this.playerBarterStrength = amount;
    }

    public int getAmount() {
        return playerBarterStrength;
    }

    @Override
    public void accept(SavingVisitor visitor) {
        visitor.visitBarterCommand(this);
    }
}
