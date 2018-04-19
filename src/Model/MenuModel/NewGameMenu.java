package Model.MenuModel;

import Controller.GameLoop;

public class NewGameMenu extends MenuState {

    public NewGameMenu(MenuModel menuModel, GameLoop gameLoop) {
        super(menuModel, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight != 0) selectedLeftRight = 0;
        if(selectedUpDown < 0) selectedUpDown = 2;
        if(selectedUpDown > 2) selectedUpDown = 0;
    }

    @Override
    public void select() {
        switch (selectedUpDown){
            case 0:
                gameLoop.newGame(0);
                break;
            case 1:
                gameLoop.newGame(1);
                break;
            case 2:
                gameLoop.newGame(2);
                break;
        }
    }
}
