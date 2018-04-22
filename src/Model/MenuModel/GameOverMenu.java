package Model.MenuModel;

import Controller.GameLoop;
import View.MenuView.TitleScreenView;

public class GameOverMenu extends MenuState {

    public GameOverMenu(MenuModel menuModel, GameLoop gameLoop) {
        super(menuModel, gameLoop);
    }

    @Override
    public void correctParameters() {

    }

    @Override
    public void select() {
        gameLoop.setMenuState(new MainMenuState(menuModel, gameLoop), new TitleScreenView(menuModel));
        gameLoop.setMainMenuKeySet();
    }
}
