package Controller.ModelKeyAction;

import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class ScrollDownKeyAction extends ModelKeyAction {

    private MenuModel menuModel;

    public ScrollDownKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            menuModel.scrollDown();
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
