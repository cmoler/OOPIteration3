package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public class SaveGameMenu extends InGameMenuState {

    public SaveGameMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 1;
        if(selectedLeftRight > 1) selectedLeftRight = 0;
        if(selectedLeftRight == 0){
            if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
            if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
        }
        else {
            if (selectedUpDown < 0) selectedUpDown = 3;
            if (selectedUpDown > 3) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        switch (selectedLeftRight){
            case 0:
                inGameMenuBar.select(selectedUpDown);
                break;
            case 1:
                selectSave();
                break;
        }

    }

    private void selectSave(){
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
