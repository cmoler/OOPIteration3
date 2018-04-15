package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import com.sun.javafx.geom.Vec3d;
import javafx.scene.input.KeyCode;

public class MoveSKeyAction extends ModelKeyAction {

    private Entity entity;

    public MoveSKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            entity.setOrientation(Orientation.SOUTH);
            entity.addVelocity(new Vec3d(0, -1, 1));
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
