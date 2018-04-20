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
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {

        player = ((StatsMenu)menuModel.getActiveState()).getPlayer();

        int startX = Commons.SCREEN_WIDTH / 4;;
        int startY = Commons.SCREEN_HEIGHT / 4;

        int width = Commons.SCREEN_WIDTH / 16;
        int height = Commons.SCREEN_HEIGHT / 8;

        gc.fillText("Health :" + Integer.toString(player.getCurrentHealth()), startX, startY);
        gc.fillText("Mana :" + Integer.toString(player.getCurrentMana()), startX, startY+height);
        gc.fillText("Gold :" + Integer.toString(player.getCurrentGold()), startX, startY+2*height);

    }
}
