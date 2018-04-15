package Controller.ModelKeyAction;

import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;
import javafx.scene.input.KeyCode;

public class MoveNWKeyAction extends ModelKeyAction {

    private Entity entity;

    public MoveNWKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            entity.addVelocity(new Vec3d(-1, 1, 0));
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
