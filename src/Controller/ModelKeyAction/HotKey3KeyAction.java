package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class HotKey3KeyAction extends ModelKeyAction {

    private Entity entity;

    public HotKey3KeyAction(KeyCode keyCode, Entity entity){
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            entity.useHotBar(3);
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
