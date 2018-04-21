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

public class TitleScreenView extends MenuViewState{

    private int selectedY;
    private int selectedX;
    private Image selected;

    public TitleScreenView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
        gc.restore();


        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int startX = Configs.Commons.SCREEN_WIDTH/2 - Commons.SCREEN_WIDTH / 12;
        int startY = Commons.SCREEN_HEIGHT/3;

        int boxWidth = Commons.SCREEN_WIDTH / 6;
        int boxHeight = Commons.SCREEN_HEIGHT / 10;

        gc.setFill(Color.GREEN);
        gc.fillRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        gc.setFont(new Font(30.0f).font("System", FontWeight.BOLD, 30.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.fillText("HYPED MEAN PENGUINS BACK FOR DAVE SMALL DAVE MEDIUM DAVE LARGE DAVE MORE",
                Commons.SCREEN_WIDTH / 30, Commons.SCREEN_HEIGHT / 7);
        gc.strokeRect(Commons.SCREEN_WIDTH / 35, Commons.SCREEN_HEIGHT / 12, 29 * Commons.SCREEN_WIDTH / 30, Commons.SCREEN_HEIGHT / 12);


        gc.setFont(new Font(30.0f).font("System", FontWeight.BOLD, 30.0f));
        gc.fillText("New Game", (startX+ boxWidth / 5), (startY+3*boxHeight/5));
        gc.fillText("Load Game", (startX + boxWidth / 5), (startY + boxHeight+3*boxHeight/5));
        gc.fillText("Options", (startX+ boxWidth / 5), (startY + 2*boxHeight+3*boxHeight/5));
        gc.fillText("Exit Game", (startX+ boxWidth / 5), (startY + 3*boxHeight+3*boxHeight/5));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.strokeRect(startX, startY, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + boxHeight, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + 2*boxHeight, boxWidth, boxHeight);
        gc.strokeRect(startX, startY + 3*boxHeight, boxWidth, boxHeight);

        int selectionBoxX = startX;
        int selectionBoxY = startY + selectedY*boxHeight;

        gc.drawImage(selected, selectionBoxX, selectionBoxY, boxWidth, boxHeight);
    }
}
