package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public class StatsMenu extends InGameMenuState {

    public StatsMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight != 0) selectedLeftRight = 0;
        if (selectedUpDown < 0) selectedUpDown = 3;
        if (selectedUpDown > 3) selectedUpDown = 0;
    }

    @Override
    public void select() {
        switch (selectedUpDown){
            case 0:
                menuModel.setActiveState(new InventoryMenu(menuModel, player, gameLoop));
                break;
            case 1:
                menuModel.setActiveState(new StatsMenu(menuModel, player, gameLoop));
                break;
            case 2:
                menuModel.setActiveState(new LevelUpMenu(menuModel, player, gameLoop));
                break;
            case 3:
                menuModel.setActiveState(new ExitGameMenu(menuModel, player, gameLoop));
                break;
        }
    }
}
