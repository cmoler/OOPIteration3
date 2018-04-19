package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;

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

        int sizeOfSaveSlotX = (TextBoxInfo.TEXTBOX_WIDTH);
        int sizeOfSaveSlotY = (Commons.SCREEN_WIDTH/4)/numberOfSaves;

        for(int i = 0; i < numberOfSaves; ++i){
            gc.rect(startX, startY + sizeOfSaveSlotY * i, sizeOfSaveSlotX, sizeOfSaveSlotY);
            gc.fillText("Save "+(i+1), (startX), ( startY + sizeOfSaveSlotY * i+TextBoxInfo.TEXTBOX_HEIGHT/4));
        }

        int selectionBoxY = startY + selectedY * sizeOfSaveSlotY;

        gc.drawImage(selected, startX, selectionBoxY, sizeOfSaveSlotX, sizeOfSaveSlotY);
    }
}
