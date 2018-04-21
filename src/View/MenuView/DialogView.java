package View.MenuView;

import Configs.Commons;
import Model.MenuModel.BarterMenu;
import Model.MenuModel.DialogMenu;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;
import java.util.Random;

public class DialogView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private String niceDialog;
    private String meanDialog;
    private Image selected;

    public DialogView(MenuModel menuModel) {
        super(menuModel);

        niceDialog = randomFriendlyString();
        meanDialog = randomMeanString();

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    public void render(GraphicsContext gc) {
        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int startX = Commons.SCREEN_WIDTH / 4;
        int startY = 2 * Commons.SCREEN_HEIGHT / 3;

        int width = Commons.SCREEN_WIDTH / 2;
        int height = Commons.SCREEN_HEIGHT / 7;

        gc.clearRect(startX, startY, width, height*2);

        boolean wantToTalk = ((DialogMenu)menuModel.getActiveState()).getWantToTalk();
        String dialog;

        if(wantToTalk) dialog = niceDialog;
        else dialog = meanDialog;

        gc.setFont(new Font(40.0f));
        gc.setFill(Color.BLACK);
        gc.fillText(dialog, startX+ width / 12, startY+4*height/6);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.strokeRect(startX, startY, width, height);
        gc.strokeRect(startX, startY + height, width / 2, height);
        gc.strokeRect(startX + width / 2, startY + height, width / 2, height);

        gc.fillText("Close", startX + width / 6, startY+height+4*height/6);
        if(wantToTalk) gc.fillText("Trade", startX + width / 2 + width / 6, startY+height+4*height/6);

        gc.drawImage(selected, startX + selectedX * width / 2, startY + height, width / 2, height);
    }

    private String randomFriendlyString() {
        Random random = new Random();
        switch (random.nextInt(10)){
            case 0:
                return "Hello";
            case 1:
                return "Whats up";
            case 2:
                return "Im friendly";
            case 3:
                return "Want to trade?";
            case 4:
                return "Who are you?";
            case 5:
                return "Have a good day";
            case 6:
                return "Yo";
            case 7:
                return "Salutations";
            case 8:
                return "Howdy";
            case 9:
                return "Good morrow sir";
        }
        return "hi";
    }

    private String randomMeanString() {
        Random random = new Random();
        switch (random.nextInt(4)){
            case 0:
                return "I will drink from your skull";
            case 1:
                return "Prepare to die";
            case 2:
                return "...";
            case 3:
                return "Why are you talking to me?";
        }
        return "fuck you";
    }
}
