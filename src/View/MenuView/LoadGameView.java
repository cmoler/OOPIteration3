package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class LoadGameView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public LoadGameView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());

    }

    @Override
    public void render(GraphicsContext gc) {
        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int topStartX = Configs.Commons.SCREEN_WIDTH/4;
        int topStartY = Commons.SCREEN_HEIGHT / 20;
        int topWidth = Commons.SCREEN_WIDTH / 2;
        int topHeight = Commons.SCREEN_HEIGHT / 8;
        gc.setFill(Color.GREEN);
        gc.fillRect(topStartX, topStartY, topWidth, topHeight);

        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(60.0f).font("System", FontWeight.BOLD, 60.0f));
        gc.fillText("Load a Game", (topStartX + topWidth / 4), (topStartY + 4*topHeight/5));

        int numberOfSaves = Commons.MAX_SAVE_SLOTS;

        int saveSlotWidth = Commons.SCREEN_WIDTH / 5;
        int saveSlotHeight = Commons.SCREEN_WIDTH/4/numberOfSaves;
        int skillsStartX = Configs.Commons.SCREEN_WIDTH / 2 - saveSlotWidth / 2;
        int skillStartY = Commons.SCREEN_HEIGHT / 3;
        gc.setFill(Color.GREEN);
        gc.fillRect(skillsStartX, skillStartY, saveSlotWidth, saveSlotHeight * numberOfSaves);

        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(60.0f).font("System", FontWeight.BOLD, 60.0f));
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        for(int i = 0; i < numberOfSaves; ++i){
            gc.strokeRect(skillsStartX, skillStartY + saveSlotHeight * i, saveSlotWidth, saveSlotHeight);
            gc.fillText("Save "+(i+1), (skillsStartX + saveSlotWidth / 8), (skillStartY + i * saveSlotHeight+4*saveSlotHeight/5));
        }

        int selectionBoxY = skillStartY + selectedY * saveSlotHeight;

        gc.drawImage(selected, skillsStartX, selectionBoxY, saveSlotWidth, saveSlotHeight);

    }
}
