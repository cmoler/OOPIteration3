package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollRightKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity entity;

    public ScrollRightKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    public ScrollRightKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(menuModel != null) menuModel.scrollRight();
            if(entity != null) entity.scrollRight();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
