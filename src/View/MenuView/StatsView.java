package View.MenuView;

import Configs.Commons;
import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import Model.MenuModel.StatsMenu;
import javafx.scene.canvas.GraphicsContext;

public class StatsView extends InGameMenuView {

    Entity player;

    public StatsView(MenuModel menuModel) {
        super(menuModel);
        player = ((StatsMenu)menuModel.getActiveState()).getPlayer();
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        int startX = Commons.SCREEN_WIDTH / 4;;
        int startY = 0;

        gc.fillText("Health :" + Integer.toString(player.getCurrentHealth()), Commons.SCREEN_WIDTH/ 2, Commons.SCREEN_HEIGHT / 4);
        gc.fillText("Mana :" + Integer.toString(player.getCurrentMana()), Commons.SCREEN_WIDTH/ 2, Commons.SCREEN_HEIGHT / 4);
        gc.fillText("Gold :" + Integer.toString(player.getCurrentGold()), Commons.SCREEN_WIDTH/ 2, Commons.SCREEN_HEIGHT / 4);

    }
}
