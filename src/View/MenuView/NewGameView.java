package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.Item.TakeableItem.ConsumableItem;
import Model.MenuModel.KeyBindings;
import Model.MenuModel.MenuModel;
import View.Sprites;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

import java.io.File;

public class NewGameView extends MenuViewState {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public NewGameView(MenuModel menuModel) {
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
        gc.fillText("Select Your Class", (topStartX + topWidth / 8), (topStartY + 4*topHeight/5));


        int bottomLeftStartX = Configs.Commons.SCREEN_WIDTH / 18;
        int bottomLeftStartY = Commons.SCREEN_HEIGHT / 4;
        int bottomLeftWidth = 5*Commons.SCREEN_WIDTH / 18;
        int bottomLeftHeight = Commons.SCREEN_HEIGHT / 5;
        gc.setFill(Color.GREEN);
        gc.fillRect(bottomLeftStartX, bottomLeftStartY, bottomLeftWidth, bottomLeftHeight * 3);

        gc.setFill(Color.WHITESMOKE);
        gc.setFont(new Font(60.0f).font("System", FontWeight.BOLD, 60.0f));
        gc.fillText("Smasher", (bottomLeftStartX + bottomLeftWidth / 8), (bottomLeftStartY + 3*bottomLeftHeight/5));
        gc.fillText("Summoner", (bottomLeftStartX + bottomLeftWidth / 8), (bottomLeftStartY + bottomLeftHeight+3*bottomLeftHeight/5));
        gc.fillText("Sneak", (bottomLeftStartX + bottomLeftWidth / 8), (bottomLeftStartY + 2*bottomLeftHeight+3*bottomLeftHeight/5));

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.strokeRect(bottomLeftStartX, bottomLeftStartY, bottomLeftWidth, bottomLeftHeight);
        gc.strokeRect(bottomLeftStartX, bottomLeftStartY + bottomLeftHeight, bottomLeftWidth, bottomLeftHeight);
        gc.strokeRect(bottomLeftStartX, bottomLeftStartY + 2*bottomLeftHeight, bottomLeftWidth, bottomLeftHeight);

        int selectionBoxY = bottomLeftStartY + selectedY * bottomLeftHeight;

        gc.drawImage(selected, bottomLeftStartX, selectionBoxY, bottomLeftWidth, bottomLeftHeight);

        Image playerSprite = null;
        switch (selectedY){
            case 0:
                playerSprite = Sprites.getInstance().getSmasherSprite();
                break;
            case 1:
                playerSprite = Sprites.getInstance().getSummonerSprite();
                break;
            case 2:
                playerSprite = Sprites.getInstance().getSneakSprite();
                break;
        }

        //Rotate 180 degrees so sprite is facing downwards
        rotate(gc, 180, Commons.SCREEN_WIDTH/2 + 150, Commons.SCREEN_HEIGHT*1/2 + 100);

        //Render player sprite
        gc.drawImage(playerSprite, Commons.SCREEN_WIDTH/2, Commons.SCREEN_HEIGHT/2, 300, 300);

        //Set rotate back to normal
        rotate(gc, 0, 0, 0);

    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
