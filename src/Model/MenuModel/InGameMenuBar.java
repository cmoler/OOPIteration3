package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import View.MenuView.*;

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
                gameLoop.setMenuState(new StatsMenu(menuModel, player, gameLoop), new StatsView(menuModel));
                break;
            case 1:
                gameLoop.setMenuState(new InventoryMenu(menuModel, player, gameLoop), new InventoryView(menuModel));
                break;
            case 2:
                gameLoop.setMenuState(new LevelUpMenu(menuModel, player, gameLoop), new LevelUpView(menuModel));
                break;
            case 3:
                gameLoop.setMenuState(new SaveGameMenu(menuModel, player, gameLoop), new SaveGameView(menuModel));
                break;
            case 4:
                gameLoop.setMenuState(new ExitGameMenu(menuModel, player, gameLoop), new ExitGameView(menuModel));
                break;
        }
    }
}
