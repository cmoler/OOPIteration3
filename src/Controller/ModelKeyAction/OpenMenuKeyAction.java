package Controller.ModelKeyAction;

import Model.MenuModel.InGameMainMenu;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class OpenMenuKeyAction extends ModelKeyAction {

    private MenuModel menuModel;

    public OpenMenuKeyAction(KeyCode keyCode, MenuModel menuModel) {
        super(keyCode);
        this.menuModel = menuModel;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            menuModel.setActiveState(new InGameMainMenu(menuModel));
        }
    }

    @Override
    public String getName() {
        return "openMenu";
    }
}
