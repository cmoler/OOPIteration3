package Controller.ModelKeyAction;

import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class SelectKeyAction extends ModelKeyAction {

    private MenuModel menuModel;

    public SelectKeyAction(KeyCode keyCode, MenuModel menuModel) {
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
