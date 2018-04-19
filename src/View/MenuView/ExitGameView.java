package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class ExitGameView extends InGameMenuView {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public ExitGameView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLUE_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int startX = Configs.Commons.SCREEN_WIDTH/2 - TextBoxInfo.TEXTBOX_WIDTH;
        int startY = Commons.SCREEN_HEIGHT/4;

        gc.fillText("Are you Sure, unsaved changes wont be saved", (startX), (startY));

        gc.rect(startX, startY + TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("No",startX, startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4);

        gc.rect(startX + TextBoxInfo.TEXTBOX_WIDTH, startY + TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Exit Game", (startX + TextBoxInfo.TEXTBOX_WIDTH), (startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        int selectedXPos = startX + TextBoxInfo.TEXTBOX_HEIGHT * selectedX;
        int selectedYPos = startY + TextBoxInfo.TEXTBOX_HEIGHT;

        if(selectedX != 0)
        gc.drawImage(selected, selectedXPos, selectedYPos, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
    }
}
