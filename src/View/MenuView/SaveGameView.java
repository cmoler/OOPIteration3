package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class SaveGameView extends InGameMenuView {

    public SaveGameView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {

        int startX = Configs.Commons.SCREEN_WIDTH/2 - TextBoxInfo.TEXTBOX_WIDTH;
        int startY = Commons.SCREEN_HEIGHT/4;

        selectedY = menuModel.getSelectedVertical();

        int numberOfSaves = Commons.MAX_SAVE_SLOTS;

        int boxWidth = Commons.SCREEN_WIDTH / 4;
        int sizeOfSaveSlotY = (Commons.SCREEN_WIDTH/4)/numberOfSaves;

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(startX, startY, boxWidth, sizeOfSaveSlotY * numberOfSaves);

        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        for(int i = 0; i < numberOfSaves; ++i){
            gc.strokeRect(startX, startY + sizeOfSaveSlotY * i, boxWidth, sizeOfSaveSlotY);
            gc.fillText("Save "+(i+1), (startX + boxWidth / 4), ( startY + sizeOfSaveSlotY * i + 3*sizeOfSaveSlotY/5));
        }

        int selectionBoxY = startY + selectedY * sizeOfSaveSlotY;

        if(selectedX != 0) gc.drawImage(selected, startX, selectionBoxY, boxWidth, sizeOfSaveSlotY);
    }
}
