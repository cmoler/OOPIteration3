package View.MenuView;

import Configs.Commons;
import Model.Entity.EntityAttributes.Inventory;
import Model.Item.TakeableItem.TakeableItem;
import Model.MenuModel.InventoryMenu;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class InventoryView extends InGameMenuView {


    public InventoryView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {
        Inventory inventory = ((InventoryMenu)menuModel.getActiveState()).getInventory();

        int startX = Commons.SCREEN_WIDTH / 4 + 20;
        int startY = Commons.SCREEN_HEIGHT / 60;

        int width = Commons.SCREEN_WIDTH / 4;
        int height = 59 * Commons.SCREEN_HEIGHT / 60 / inventory.size();

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        for(int i = 0; i < inventory.size(); ++i){
            gc.fillText(inventory.getItem(i).getName(), startX+ width / 6, startY + i * height+4*height/6);
            gc.strokeRect(startX, startY + i * height, width, height);
        }

        if(selectedX == 1){
            gc.drawImage(selected, startX, startY + selectedY * height, width, height);
        }

        if(selectedX != 0){
            drawItemDetails(gc);
        }
    }

    private void drawItemDetails(GraphicsContext gc) {

        TakeableItem takeableItem = ((InventoryMenu)menuModel.getActiveState()).getSelectedItem();


        int startX = 3 * Commons.SCREEN_WIDTH / 5;
        int startY = Commons.SCREEN_HEIGHT / 4;

        //draw item picture

        //draw item name
        gc.fillText(takeableItem.getName(), startX, startY);

        int width = Commons.SCREEN_WIDTH / 8;
        int height = Commons.SCREEN_HEIGHT / 16;

        int optionsStartX = startX;
        int optionsStartY = Commons.SCREEN_HEIGHT / 2;

        //draw use
        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.fillText("use", (optionsStartX+ width / 6), (optionsStartY+4*height/5));
        gc.fillText("assign", (optionsStartX + width / 6), (optionsStartY + height+4*height/5));
        gc.fillText("drop", (optionsStartX+ width / 6), (optionsStartY + 2*height+4*height/5));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5.0f);
        gc.strokeRect(optionsStartX, optionsStartY, width, height);
        gc.strokeRect(optionsStartX, optionsStartY + height, width, height);
        gc.strokeRect(optionsStartX, optionsStartY + 2*height, width, height);

        //draw selector
        if(selectedX == 2) gc.drawImage(selected, optionsStartX, optionsStartY + selectedY*height, width, height);
    }
}
