package Controller.ModelKeyAction;

import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollLeftKeyAction extends ModelKeyAction {

    private MenuModel menuModel;

    public ScrollLeftKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
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
