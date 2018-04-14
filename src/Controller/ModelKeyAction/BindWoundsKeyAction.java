package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class BindWoundsKeyAction extends ModelKeyAction {

    private Entity entity;

    public BindWoundsKeyAction(KeyCode keyCode, Entity entity){
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
        return null;
    }
}
