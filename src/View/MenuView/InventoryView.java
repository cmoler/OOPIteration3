package View.MenuView;

import Configs.Commons;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.TakeableItem.TakeableItem;
import Model.MenuModel.InventoryMenu;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;

public class InventoryView extends InGameMenuView {


    public InventoryView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {
        Inventory inventory = ((InventoryMenu)menuModel.getActiveState()).getInventory();

        int startX = Commons.SCREEN_WIDTH / 2;
        int startY = Commons.SCREEN_HEIGHT / 4;

        int width = Commons.SCREEN_WIDTH / 4;
        int height = Commons.SCREEN_HEIGHT / inventory.size();

        for(int i = 0; i < inventory.size(); ++i){
            gc.fillText(inventory.getItem(i).getName(), startX, startY + i * height);
            gc.rect(startX, startY + i * height, width, height);
        }

        if(selectedX == 1){
            gc.drawImage(selected, startX, startY + selectedY * height, width, height);
        }

        if(selectedX == 2){
            drawItemDetails(gc);
        }
    }

    private void drawItemDetails(GraphicsContext gc) {

        TakeableItem takeableItem = ((InventoryMenu)menuModel.getActiveState()).getSelectedItem();


        int startX = 3 * Commons.SCREEN_WIDTH / 4;
        int startY = Commons.SCREEN_HEIGHT / 4;

        //draw item picture

        //draw item name
        gc.fillText(takeableItem.getName(), startX, startY);

        int width = Commons.SCREEN_WIDTH / 16;
        int height = Commons.SCREEN_HEIGHT / 16;

        //draw use
        gc.fillText("use", startX, Commons.SCREEN_HEIGHT / 2);
        gc.rect(startX, Commons.SCREEN_HEIGHT / 2, width, height);

        // draw assign
        gc.fillText("assign", startX, Commons.SCREEN_HEIGHT / 2 + height);
        gc.rect(startX, Commons.SCREEN_HEIGHT / 2 + height, width, height);

        //draw drop
        gc.fillText("drop", startX, Commons.SCREEN_HEIGHT / 2 + 2*height);
        gc.rect(startX, Commons.SCREEN_HEIGHT / 2 + 2*height, width, height);

        //draw selector
        gc.drawImage(selected, startX, startY + selectedY*height, width, height);
    }
}
