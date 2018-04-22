package View.LevelView;

import Configs.Commons;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.ItemHotBar;
import Model.Entity.EntityAttributes.Skill;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class HotbarView {
    private Entity player;
    private int itemSize;
    private int itemHotBarStartY;
    public HotbarView(Entity player) {
        this.player = player;
        itemSize = 75;
        itemHotBarStartY = (int)((Commons.SCREEN_HEIGHT/2) - (itemSize*2.5));
    }

    public void render(GraphicsContext gc) {
        ItemHotBar hotBar = player.getItemHotBar();

        renderItemHotBarOverlay(gc);
        renderHotBarItems(gc, hotBar);
        
        renderSkillBarOverlay(gc, player.getNonWeaponSkills());
        renderSkillBarItems(gc, player.getNonWeaponSkills());
    }

    private void renderItemHotBarOverlay(GraphicsContext gc) {

        gc.setFill(Color.GRAY);
        gc.fillRect(Commons.SCREEN_WIDTH-itemSize, itemHotBarStartY, itemSize, itemSize*5);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 16));
        for(int i = 0; i < 5; i++) {
            if(i == player.getCurrentlySelectedItemIndex()) {
                gc.setFill(Color.RED);
                gc.fillRect(Commons.SCREEN_WIDTH-itemSize, itemHotBarStartY + (itemSize*i), itemSize , itemSize);
            }
            renderSquareBorder(gc, Commons.SCREEN_WIDTH-itemSize, itemHotBarStartY + (itemSize*i), itemSize);

            gc.fillText(Integer.toString(i+1), Commons.SCREEN_WIDTH-itemSize+5, itemHotBarStartY + (itemSize*i) + 20);
        }

    }

    private void renderHotBarItems(GraphicsContext gc, ItemHotBar hotBar) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 10));

        for(int i = 0; i < 5; i++) {
            if(hotBar.getItem(i) == null) {
                gc.fillText("Empty", Commons.SCREEN_WIDTH-itemSize+15, itemHotBarStartY + (itemSize*i) + 40);
            } else {
                gc.fillText(hotBar.getItem(i).getName(), Commons.SCREEN_WIDTH - itemSize + 15, itemHotBarStartY + (itemSize * i) + 40);
            }
        }
    }

    private void renderSkillBarOverlay(GraphicsContext gc, List<Skill> playerSkills) {


        gc.setFill(Color.GRAY);
        gc.fillRect(Commons.SCREEN_WIDTH/2 - (itemSize*playerSkills.size())/2, 0, itemSize*playerSkills.size(), itemSize);


        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 16));
        for(int i = 0; i < playerSkills.size(); i++) {
            renderSquareBorder(gc, (Commons.SCREEN_WIDTH/2)-(itemSize*playerSkills.size())/2 + itemSize*i, 0, itemSize);
            gc.setFill(Color.WHITE);
            gc.fillText(Integer.toString(i+1), (Commons.SCREEN_WIDTH/2)-(itemSize*playerSkills.size())/2 + (itemSize*i) + 10, 20);
        }

    }

    private void renderSkillBarItems(GraphicsContext gc, List<Skill> playerSkills) {
        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 10));
        for(int i = 0; i < playerSkills.size(); i++) {

            gc.fillText(playerSkills.get(i).getName(), (Commons.SCREEN_WIDTH/2)-(itemSize*playerSkills.size())/2 + (itemSize*i) + 25, 30);

        }
    }

    private void renderSquareBorder(GraphicsContext gc, int x, int y, int size) {

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(3);
        gc.strokeLine(x, y, x+size, y);
        gc.strokeLine(x+size, y, x+size, y+size);
        gc.strokeLine(x+size, y+size, x, y+size);
        gc.strokeLine(x, y+size, x, y);
    }
}
