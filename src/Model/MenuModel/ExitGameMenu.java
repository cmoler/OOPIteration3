package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import View.MenuView.StatsView;
import View.MenuView.TitleScreenView;

public class ExitGameMenu extends InGameMenuState {

    public ExitGameMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedUpDown != 0) selectedUpDown = 0;
        if(selectedLeftRight < 0) selectedLeftRight = 2;
        if(selectedLeftRight > 2) selectedLeftRight = 0;
    }

    @Override
    public void select() {
        switch (selectedLeftRight){
            case 0:
                gameLoop.setMenuState(new MainMenuState(menuModel, gameLoop), new TitleScreenView(menuModel));
                gameLoop.setMainMenuKeySet();
                break;
            case 1:
                gameLoop.setMenuState(new StatsMenu(menuModel, player, gameLoop), new StatsView(menuModel));
                gameLoop.setInGameMenuKeySet();
                break;
            case 2:
                System.exit(1);
                break;
        }
    }
}
