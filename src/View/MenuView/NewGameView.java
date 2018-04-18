package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.KeyBindings;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class NewGameView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public NewGameView(MenuModel menuModel) {
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

        int startX = Configs.Commons.SCREEN_WIDTH/2 - TextBoxInfo.TEXTBOX_WIDTH;
        int startY = Commons.SCREEN_HEIGHT/8;

        gc.fillText("Select Your Class", (startX), (startY + TextBoxInfo.TEXTBOX_HEIGHT/2));

        gc.rect(startX, startY + TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Smasher",startX , startY + TextBoxInfo.TEXTBOX_HEIGHT + TextBoxInfo.TEXTBOX_HEIGHT / 2);

        gc.rect(startX, startY + 2 * TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Summoner",startX,startY + TextBoxInfo.TEXTBOX_HEIGHT * 2 + TextBoxInfo.TEXTBOX_HEIGHT / 2);

        gc.rect(startX, startY + 3 * TextBoxInfo.TEXTBOX_HEIGHT, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
        gc.fillText("Sneak",startX,startY + TextBoxInfo.TEXTBOX_HEIGHT * 3 + TextBoxInfo.TEXTBOX_HEIGHT / 2);

        int selectionBoxY = startY + TextBoxInfo.TEXTBOX_HEIGHT + selectedY * TextBoxInfo.TEXTBOX_WIDTH;

        gc.drawImage(selected, startX, selectionBoxY, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);

    }
}
