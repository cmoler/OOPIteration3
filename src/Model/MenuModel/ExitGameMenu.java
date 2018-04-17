package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

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
                menuModel.setActiveState(new MainMenuState(menuModel, gameLoop));
                break;
            case 1:
                menuModel.setActiveState(new InGameMainMenu(menuModel, player, gameLoop));
                break;
            case 2:
                System.exit(1);
                break;
        }
    }
}
