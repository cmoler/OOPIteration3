package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollLeftKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity entity;

    public ScrollLeftKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    public ScrollLeftKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(menuModel != null) menuModel.scrollLeft();
            if(entity != null) entity.scrollLeft();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
