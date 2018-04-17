package Model.MenuModel;

import Controller.GameLoop;

public class SaveGameMenu extends MenuState {

    public SaveGameMenu(MenuModel menuModel, GameLoop gameLoop) {
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
                gameLoop.saveGame(0);
                break;
            case 1:
                gameLoop.saveGame(1);
                break;
            case 2:
                gameLoop.saveGame(2);
                break;
            case 3:
                gameLoop.saveGame(3);
                break;
        }
    }
}
