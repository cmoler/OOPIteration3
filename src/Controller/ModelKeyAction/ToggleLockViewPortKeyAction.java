package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class ToggleLockViewPortKeyAction extends ModelKeyAction {

    private Entity entity;

    public ToggleLockViewPortKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){

        }
    }

    @Override
    public String getName() {
        return "toggleLockView";
    }
}
