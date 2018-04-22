package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollUpKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity entity;

    public ScrollUpKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    public ScrollUpKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(menuModel != null) menuModel.scrollUp();
            if(entity != null) entity.scrollUp();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
