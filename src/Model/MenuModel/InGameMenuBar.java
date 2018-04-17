package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public class InGameMenuBar {

    private MenuModel menuModel;
    private Entity player;
    private GameLoop gameLoop;

    private int maxUp = 4;

    public InGameMenuBar(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        this.menuModel = menuModel;
        this.player = player;
        this.gameLoop = gameLoop;
    }

    public int getMaxUp(){
        return maxUp;
    }

    public void select(int selected) {
        switch (selected){
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
            case 4:
                menuModel.setActiveState(new SaveGameMenu(menuModel, player, gameLoop));
        }
    }
}
