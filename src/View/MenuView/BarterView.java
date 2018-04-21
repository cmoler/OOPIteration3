package View.MenuView;

import Configs.Commons;
import Model.Entity.EntityAttributes.Inventory;
import Model.MenuModel.BarterMenu;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class BarterView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public BarterView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {
        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int startX = Commons.SCREEN_WIDTH / 3;
        int startY = Commons.SCREEN_HEIGHT / 20;
        int width = Commons.SCREEN_WIDTH / 3;
        int height = Commons.SCREEN_HEIGHT / 12;

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, width, height);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.strokeRect(startX, startY, width, height);

        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(60.0f).font("System", FontWeight.BOLD, 60.0f));
        gc.fillText("Barter", startX + width / 3, startY + 4*height/5);

        renderPlayer(gc);
        renderNPC(gc);
    }

    private void renderPlayer(GraphicsContext gc) {
        Inventory playerInventory = ((BarterMenu)menuModel.getActiveState()).getPlayerInventory();
        int startX = Commons.SCREEN_WIDTH / 6;
        int startY = Commons.SCREEN_HEIGHT / 6;
        int width = Commons.SCREEN_WIDTH / 4;
        int totalHeight = 3 * Commons.SCREEN_HEIGHT / 4;

        int itemHeight;
        if(playerInventory.size() == 0) itemHeight = 0;
        else itemHeight = totalHeight / playerInventory.size();

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, width, totalHeight);

        gc.setFont(new Font(40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        for(int i = 0; i < playerInventory.size(); ++i){
            gc.fillText(playerInventory.getItem(i).getName(), startX+ width / 6, startY + i * itemHeight+4*itemHeight/6);
            gc.strokeRect(startX, startY + i * itemHeight, width, itemHeight);
        }

        if(selectedX == 0){
            gc.drawImage(selected, startX, startY + selectedY * itemHeight, width, itemHeight);
        }
    }

    private void renderNPC(GraphicsContext gc) {
        Inventory npcInventory = ((BarterMenu)menuModel.getActiveState()).getNpcInventory();
        int startX = 3 * Commons.SCREEN_WIDTH / 5;
        int startY = Commons.SCREEN_HEIGHT / 6;
        int width = Commons.SCREEN_WIDTH / 4;
        int totalHeight = 3 * Commons.SCREEN_HEIGHT / 4;

        int itemHeight;
        if(npcInventory.size() == 0) itemHeight = 0;
        else itemHeight = totalHeight / npcInventory.size();

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, width, totalHeight);

        gc.setFont(new Font(40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        for(int i = 0; i < npcInventory.size(); ++i){
            gc.fillText(npcInventory.getItem(i).getName(), startX+ width / 6, startY + i * itemHeight+4*itemHeight/6);
            gc.strokeRect(startX, startY + i * itemHeight, width, itemHeight);
        }

        if(selectedX == 1){
            gc.drawImage(selected, startX, startY + selectedY * itemHeight, width, itemHeight);
        }
    }
}
