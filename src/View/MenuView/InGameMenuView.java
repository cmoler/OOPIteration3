package View.MenuView;

import Configs.Commons;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;

public abstract class InGameMenuView extends MenuViewState {

    protected int selectedY;
    protected int selectedX;
    protected Image selected;

    public InGameMenuView(MenuModel menuModel) {
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

        int startX = 0;
        int startY = 0;

        int boxWidth = Commons.SCREEN_WIDTH / 4;
        int boxHeight = Commons.SCREEN_HEIGHT / 5;

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, boxWidth, Commons.SCREEN_HEIGHT);

        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(boxWidth, 0, 3 * Commons.SCREEN_WIDTH/4, Commons.SCREEN_HEIGHT);

        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.fillText("Stats", (startX + boxWidth / 4), (startY + 3*boxHeight/5));
        gc.fillText("Inventory", (startX + boxWidth / 4), (startY + boxHeight+3*boxHeight/5));
        gc.fillText("LevelUp", (startX + boxWidth / 4), (startY + 2*boxHeight+3*boxHeight/5));
        gc.fillText("Save", (startX + boxWidth / 4), (startY + 3*boxHeight+3*boxHeight/5));
        gc.fillText("Exit", (startX + boxWidth / 4), (startY + 4*boxHeight+3*boxHeight/5));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.strokeRect(startX, startY, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + boxHeight, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + 2*boxHeight, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + 3*boxHeight, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + 4*boxHeight, boxWidth, boxHeight);


        if(selectedX == 0) {
            int selectionBoxX = startX;
            int selectionBoxY = startY + selectedY * boxHeight;

            //gc.strokeOval(selectionBoxX, selectionBoxY, boxWidth, boxHeight);
            gc.drawImage(selected, selectionBoxX, selectionBoxY, boxWidth, boxHeight);
        }

        renderSubMenu(gc);
    }

    protected abstract void renderSubMenu(GraphicsContext gc);
}
