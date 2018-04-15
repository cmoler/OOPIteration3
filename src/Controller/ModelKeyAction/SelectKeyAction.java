package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class SelectKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity entity;

    public SelectKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    public SelectKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(menuModel != null) menuModel.select();
            if(entity != null) entity.useSkill();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
