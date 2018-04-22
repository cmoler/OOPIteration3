package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollDownKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity entity;

    public ScrollDownKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    public ScrollDownKeyAction(KeyCode keyCode, Entity entity) {
        super(keyCode);
        this.entity = entity;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(menuModel != null) menuModel.scrollDown();
            if(entity != null) entity.scrollDown();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
