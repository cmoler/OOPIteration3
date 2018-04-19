package View.MenuView;

import Configs.Commons;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;

public class AssignItemView extends MenuViewState {

    public AssignItemView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    public void render(GraphicsContext gc) {

        int width = Commons.SCREEN_WIDTH / 8;
        int height = Commons.SCREEN_HEIGHT / 8;

        int startX = Commons.SCREEN_WIDTH / 2 - Commons.SCREEN_WIDTH / 5;
        int startY = Commons.SCREEN_HEIGHT / 2 - height / 2;

        gc.fillText("1", startX, startY);
        gc.fillText("2", startX+width, startY);
        gc.fillText("3", startX+2*width, startY);
        gc.fillText("4", startX+3*width, startY);
        gc.fillText("5", startX+4*width, startY);

        gc.rect(startX, startY, width, height);
        gc.rect(startX+width, startY, width, height);
        gc.rect(startX+2*width, startY, width, height);
        gc.rect(startX+3*width, startY, width, height);
        gc.rect(startX+4*width, startY, width, height);
    }
}
