package View.MenuView;

import Configs.Commons;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameOverView extends MenuViewState {

    public GameOverView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    public void render(GraphicsContext gc) {
        int startX = Commons.SCREEN_WIDTH / 4;
        int startY = Commons.SCREEN_HEIGHT / 4;
        int width = Commons.SCREEN_WIDTH / 2;
        int height = Commons.SCREEN_HEIGHT / 2;

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, width, height);

        gc.setFill(Color.BLACK);
        gc.setFont(new Font(70.0f).font("System", FontWeight.BOLD, 70.0f));
        gc.fillText("GAME OVER", startX + width / 5, startY + 2 *height / 5);
        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.fillText("Press Enter To Continue", startX + width / 6, startY + 3 *height / 5);
    }
}
