package View.MenuView;

import Configs.Commons;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public abstract class InGameMenuView extends MenuViewState {

    protected int selectedY;
    protected int selectedX;
    protected Image selected;

    public InGameMenuView(MenuModel menuModel) {
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

        int startX = 0;
        int startY = 0;

        int boxWidth = Commons.SCREEN_WIDTH / 4;
        int boxHeight = Commons.SCREEN_HEIGHT / 5;

        gc.rect(startX, startY, boxWidth, boxHeight);
        gc.fillText("Stats", (startX), (startY+boxHeight/4));

        gc.rect(startX, startY + boxHeight, boxWidth, boxHeight);
        gc.fillText("Inventory", (startX), (startY + boxHeight+boxHeight/4));

        gc.rect(startX, startY + 2*boxHeight, boxWidth, boxHeight);
        gc.fillText("LevelUp", (startX), (startY + 2*boxHeight+boxHeight/4));

        gc.rect(startX, startY + 2*boxHeight, boxWidth, boxHeight);
        gc.fillText("Save", (startX), (startY + 3*boxHeight+boxHeight/4));

        gc.rect(startX, startY + 2*boxHeight, boxWidth, boxHeight);
        gc.fillText("Exit", (startX), (startY + 4*boxHeight+boxHeight/4));

        if(selectedX == 0) {
            int selectionBoxX = startX;
            int selectionBoxY = startY + selectedY * boxHeight;

            gc.drawImage(selected, selectionBoxX, selectionBoxY, boxWidth, boxHeight);
        }

        renderSubMenu(gc);
    }

    protected abstract void renderSubMenu(GraphicsContext gc);
}
