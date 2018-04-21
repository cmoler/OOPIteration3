package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.File;

public class ExitGameView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public ExitGameView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int topStartX = Configs.Commons.SCREEN_WIDTH/6;
        int bottomStartX = Configs.Commons.SCREEN_WIDTH / 7;
        int startY = Commons.SCREEN_HEIGHT/3;

        int width = 5*Commons.SCREEN_WIDTH / 18;

        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        gc.setFill(Color.GREEN);
        gc.fillRect(Commons.SCREEN_WIDTH / 9, Commons.SCREEN_HEIGHT / 4, 3 * Commons.SCREEN_WIDTH / 4, Commons.SCREEN_HEIGHT / 4);

        gc.setFill(Color.WHITESMOKE);

        gc.fillText("Are you Sure? Unsaved changes wont be saved.", (topStartX), (startY));

        gc.rect(bottomStartX, startY + TextBoxInfo.TEXTBOX_HEIGHT, width, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Go to Title Screen",bottomStartX, startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4);

        gc.rect(bottomStartX + width, startY + TextBoxInfo.TEXTBOX_HEIGHT, width, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Return to Game", (bottomStartX + width), (startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        gc.rect(bottomStartX + width, startY + TextBoxInfo.TEXTBOX_HEIGHT, width, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Exit Game", (bottomStartX + 2 * width), (startY + TextBoxInfo.TEXTBOX_HEIGHT+TextBoxInfo.TEXTBOX_HEIGHT/4));

        int selectedXPos = bottomStartX + width * selectedX;
        int selectedYPos = startY + TextBoxInfo.TEXTBOX_HEIGHT/2;

        if(selectedX != 2)
            gc.drawImage(selected, selectedXPos, selectedYPos, width, TextBoxInfo.TEXTBOX_HEIGHT);

        if(selectedX == 2)
            gc.drawImage(selected, selectedXPos, selectedYPos, 2 * width / 3, TextBoxInfo.TEXTBOX_HEIGHT);

    }
}
