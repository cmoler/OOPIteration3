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
        if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
        if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
    }

    @Override
    public void select() {
        inGameMenuBar.select(selectedUpDown);
    }
}
