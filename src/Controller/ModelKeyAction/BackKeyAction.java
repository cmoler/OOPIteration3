package Controller.ModelKeyAction;

import Controller.GameLoop;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import View.MenuView.TitleScreenView;
import javafx.scene.input.KeyCode;

public class BackKeyAction extends ModelKeyAction {

    private MenuModel menuModel;
    private GameLoop gameLoop;

    public BackKeyAction(KeyCode keyCode, MenuModel menuModel, GameLoop gameLoop) {
        super(keyCode);
        this.menuModel = menuModel;
        this.gameLoop = gameLoop;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            gameLoop.setMenuState(new MainMenuState(menuModel, gameLoop), new TitleScreenView(menuModel));
        }
    }

    @Override
    public String getName() {
        return "back";
    }
}
