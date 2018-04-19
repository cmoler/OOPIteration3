package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;

import javafx.scene.image.Image;

import java.io.File;

public class TitleScreenView extends MenuViewState{

    private int selectedY;
    private int selectedX;
    private Image selected;

    public TitleScreenView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLUE_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int startX = Configs.Commons.SCREEN_WIDTH/2 - TextBoxInfo.TEXTBOX_WIDTH + 28;
        int startY = Commons.SCREEN_HEIGHT/4;

        gc.rect(startX, startY, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("New Game", (startX), (startY+TextBoxInfo.TEXTBOX_HEIGHT/4));

        gc.rect(startX, startY + TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Load Game", (startX), (startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        gc.rect(startX, startY + 2*TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Options", (startX), (startY + 2*TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        gc.rect(startX, startY + 3*TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Exit Game", (startX), (startY + 3*TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        int selectionBoxX = startX;
        int selectionBoxY = startY + selectedY*TextBoxInfo.TEXTBOX_HEIGHT;

        gc.drawImage(selected, selectionBoxX, selectionBoxY, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
    }
}
