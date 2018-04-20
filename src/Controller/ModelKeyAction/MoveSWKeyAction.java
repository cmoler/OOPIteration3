package Controller.ModelKeyAction;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

public class MoveSWKeyAction extends ModelKeyAction {

    private Entity entity;
    private GameLoop gameLoop;

    public MoveSWKeyAction(KeyCode keyCode, Entity entity, GameLoop gameLoop) {
        super(keyCode);
        this.entity = entity;
        this.gameLoop = gameLoop;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            entity.setOrientation(Orientation.SOUTHWEST);
            entity.addVelocity(new Vec3d(-1, 0 , 1));
            gameLoop.setScrollOffSet(Point2D.ZERO);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
