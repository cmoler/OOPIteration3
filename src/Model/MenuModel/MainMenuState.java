package Model.MenuModel;

import Controller.GameLoop;

public class MainMenuState extends MenuState {

    public MainMenuState(MenuModel menuModel, GameLoop gameLoop) {
        super(menuModel, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight != 0) selectedLeftRight = 0;
        if(selectedUpDown < 0) selectedUpDown = 3;
        if(selectedUpDown > 3) selectedUpDown = 0;
    }

    @Override
    public void select() {
        switch (selectedUpDown){
            case 0:
                menuModel.setActiveState(new NewGameMenu(menuModel, gameLoop));
                break;
            case 1:
                menuModel.setActiveState(new LoadGameMenu(menuModel, gameLoop));
                break;
            case 2:
                menuModel.setActiveState(new OptionsMenu(menuModel, gameLoop));
                break;
            case 3:
                System.exit(1);
                break;
        }
    }
}
