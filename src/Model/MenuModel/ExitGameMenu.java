package Model.MenuModel;

import Model.Entity.Entity;

public class ExitGameMenu extends InGameMenuState {

    public ExitGameMenu(MenuModel menuModel, Entity player) {
        super(menuModel, player);
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
                menuModel.setActiveState(new MainMenuState(menuModel));
                break;
            case 1:
                menuModel.setActiveState(new InGameMainMenu(menuModel, player));
                break;
            case 2:
                System.exit(1);
                break;
        }
    }
}
