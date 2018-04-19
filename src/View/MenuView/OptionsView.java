package View.MenuView;

import Configs.Commons;
import Model.MenuModel.KeyBindings;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class OptionsView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private KeyBindings keyBindings;
    private Image selected;

    public OptionsView(MenuModel menuModel) {
        super(menuModel);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLUE_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        keyBindings = new KeyBindings();

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();
        drawHeader(gc);
        drawBody(gc);
    }

    private void drawHeader(GraphicsContext gc){
        int startX = Configs.Commons.SCREEN_WIDTH/4;
        int startY = Commons.SCREEN_HEIGHT/8;

        int numberOfBindings = keyBindings.getNumberOfBindings();

        int sizeOfBindingHeaderBox = (Commons.SCREEN_WIDTH/2)/numberOfBindings;
        int sizeOfBindingHeaderBoxY = (Commons.SCREEN_HEIGHT/16);

        for(int i = 0; i < numberOfBindings; ++i){
            gc.rect(startX + sizeOfBindingHeaderBox * i, startY, sizeOfBindingHeaderBox, sizeOfBindingHeaderBoxY);
            gc.fillText(keyBindings.getBinding(i), (startX + sizeOfBindingHeaderBox * i), (startY + sizeOfBindingHeaderBoxY / 2));
        }

        int selectionBoxX = startX + selectedX * sizeOfBindingHeaderBox;
        gc.drawImage(selected, selectionBoxX, startY, sizeOfBindingHeaderBox, sizeOfBindingHeaderBoxY);
    }

    private void drawBody(GraphicsContext gc){
        int startX = Configs.Commons.SCREEN_WIDTH/4;
        int startY = Commons.SCREEN_HEIGHT/4;

        int numberOfKeys = keyBindings.getNumberOfKeysForBinding(selectedX);

        int sizeOfKeyBoxX = (Commons.SCREEN_WIDTH/2);
        int sizeOfKeyBoxY = (Commons.SCREEN_HEIGHT/2)/numberOfKeys;

        for(int i = 0; i < numberOfKeys; ++i){
            gc.rect(startX , startY + sizeOfKeyBoxY * i, sizeOfKeyBoxX, sizeOfKeyBoxY);
            gc.fillText(keyBindings.getKey(keyBindings.getBinding(selectedX), i).getKey(), startX, (startY + sizeOfKeyBoxY * i + sizeOfKeyBoxY / 2));
            gc.fillText((keyBindings.getKey(keyBindings.getBinding(selectedX), i).getValue()).getName(), startX + sizeOfKeyBoxX / 2, (startY + sizeOfKeyBoxY * i + sizeOfKeyBoxY / 2));
        }

        int selectionBoxY = startY + selectedY * sizeOfKeyBoxY;

        gc.drawImage(selected, startX, selectionBoxY, sizeOfKeyBoxX, sizeOfKeyBoxY);
    }

}
