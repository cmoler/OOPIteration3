package Model.MenuModel;

import Model.Entity.Entity;

public class StatsMenu extends InGameMenuState {

    public StatsMenu(MenuModel menuModel, Entity player) {
        super(menuModel, player);
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
                menuModel.setActiveState(new InventoryMenu(menuModel, player));
                break;
            case 1:
                menuModel.setActiveState(new StatsMenu(menuModel, player));
                break;
            case 2:
                menuModel.setActiveState(new LevelUpMenu(menuModel, player));
                break;
            case 3:
                menuModel.setActiveState(new ExitGameMenu(menuModel, player));
                break;
        }
    }
}
