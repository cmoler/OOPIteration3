package Controller.ModelKeyAction;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import Model.MenuModel.StatsMenu;
import View.MenuView.StatsView;
import javafx.scene.input.KeyCode;

public class OpenMenuKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private Entity player;
    private GameLoop gameLoop;

    public OpenMenuKeyAction(KeyCode keyCode, Entity player, MenuModel menuModel, GameLoop gameLoop) {
        super(keyCode);
        this.player = player;
        this.menuModel = menuModel;
        this.gameLoop = gameLoop;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            gameLoop.setMenuState(new StatsMenu(menuModel, player, gameLoop), new StatsView(menuModel));
            gameLoop.setInGameMenuKeySet();
        }
    }

    @Override
    public String getName() {
        return "openMenu";
    }
}
