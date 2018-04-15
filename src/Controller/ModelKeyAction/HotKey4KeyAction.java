package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class HotKey4KeyAction extends ModelKeyAction {

    private Entity entity;

    public HotKey4KeyAction(KeyCode keyCode, Entity entity){
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            entity.useSkill(4);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
