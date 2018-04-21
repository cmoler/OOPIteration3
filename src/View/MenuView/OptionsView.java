package View.MenuView;

import Configs.Commons;
import Model.MenuModel.KeyBindings;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;

public class OptionsView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private KeyBindings keyBindings;
    private Image selected;

    public OptionsView(MenuModel menuModel) {
        super(menuModel);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {

        keyBindings = new KeyBindings();

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);
        gc.setFill(Color.BLACK);
        gc.fillRect(0,0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

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
        int sizeOfBindingHeaderBoxY = (Commons.SCREEN_HEIGHT/12);
        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, Commons.SCREEN_WIDTH/2, sizeOfBindingHeaderBoxY);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));

        for(int i = 0; i < numberOfBindings; ++i){
            gc.fillText(keyBindings.getBinding(i), (startX + sizeOfBindingHeaderBox * i+ sizeOfBindingHeaderBox / 8), (startY + 3*sizeOfBindingHeaderBoxY/5));
            gc.strokeRect(startX + sizeOfBindingHeaderBox * i, startY, sizeOfBindingHeaderBox, sizeOfBindingHeaderBoxY);
        }

        int selectionBoxX = startX + selectedX * sizeOfBindingHeaderBox;
        gc.drawImage(selected, selectionBoxX, startY, sizeOfBindingHeaderBox, sizeOfBindingHeaderBoxY);
    }

    private void drawBody(GraphicsContext gc){
        int startX = Configs.Commons.SCREEN_WIDTH/4;
        int startY = Commons.SCREEN_HEIGHT/4;

        int numberOfKeys = keyBindings.getNumberOfKeysForBinding(selectedX);

        int sizeOfKeyBoxX = (Commons.SCREEN_WIDTH/2);
        int sizeOfKeyBoxY = (3 * Commons.SCREEN_HEIGHT/5)/numberOfKeys;

        gc.setFill(Color.GREEN);
        gc.fillRect(startX, startY, sizeOfKeyBoxX, sizeOfKeyBoxY * numberOfKeys);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2.0f);
        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(20.0f));

        for(int i = 0; i < numberOfKeys; ++i){
            gc.fillText(keyBindings.getKey(keyBindings.getBinding(selectedX), i).getKey(), startX + sizeOfKeyBoxX / 5, (startY + sizeOfKeyBoxY * i + 4*sizeOfKeyBoxY/5));
            gc.fillText((keyBindings.getKey(keyBindings.getBinding(selectedX), i).getValue()).getName(), startX + 4 * sizeOfKeyBoxX / 6, (startY + sizeOfKeyBoxY * i + 4*sizeOfKeyBoxY/5));
            gc.strokeRect(startX , startY + sizeOfKeyBoxY * i, sizeOfKeyBoxX, sizeOfKeyBoxY);
        }

        int selectionBoxY = startY + selectedY * sizeOfKeyBoxY;

        gc.drawImage(selected, startX, selectionBoxY, sizeOfKeyBoxX, sizeOfKeyBoxY);
    }

}
