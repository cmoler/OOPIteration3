package Controller.ModelKeyAction;

import Model.Entity.Entity;
import Model.MenuModel.InGameMainMenu;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

public class OpenMenuKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity player;

    public OpenMenuKeyAction(KeyCode keyCode, Entity player, MenuModel menuModel) {
        super(keyCode);
        this.player = player;
        this.menuModel = menuModel;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            menuModel.setActiveState(new InGameMainMenu(menuModel, player));
        }
    }

    @Override
    public String getName() {
        return "openMenu";
    }
}
