package View.MenuView;

import Configs.Commons;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class AssignItemView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public AssignItemView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int width = Commons.SCREEN_WIDTH / 8;
        int height = Commons.SCREEN_HEIGHT / 8;

        int titleStartX = Commons.SCREEN_WIDTH / 6;
        int titleStartY = Commons.SCREEN_HEIGHT / 8;

        int startX = Commons.SCREEN_WIDTH / 6;
        int startY = Commons.SCREEN_HEIGHT / 2 - height / 2;

        gc.clearRect(startX, startY, width*5, height);

        gc.setFill(Color.BLACK);
        gc.fillRect(startX, 6 * startY / 8, width * 5, height);

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, width*5, height);

        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.fillText("Assign Item to Item Hot Bar", startX + width, 11 * startY / 12);
        gc.fillText("1", startX+width / 3, startY+3*height/5);
        gc.fillText("2", startX+width + width / 3, startY+3*height/5);
        gc.fillText("3", startX+2*width + width / 3, startY+3*height/5);
        gc.fillText("4", startX+3*width + width / 3, startY+3*height/5);
        gc.fillText("5", startX+4*width + width / 3, startY+3*height/5);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5.0f);
        gc.strokeRect(startX, startY, width, height);
        gc.strokeRect(startX+width, startY, width, height);
        gc.strokeRect(startX+2*width, startY, width, height);
        gc.strokeRect(startX+3*width, startY, width, height);
        gc.strokeRect(startX+4*width, startY, width, height);

        gc.drawImage(selected, startX + selectedX*width, startY, width, height);
    }
}
