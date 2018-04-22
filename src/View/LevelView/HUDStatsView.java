package View.LevelView;

import Configs.Commons;
import Model.Entity.Entity;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

public class HUDStatsView {
    private Entity player;
    private int statBarWidth;
    private int statBarHeight;
    public HUDStatsView(Entity player) {
        this.player = player;
        statBarWidth = 200;
        statBarHeight = 20;
    }

    public void render(GraphicsContext gc) {
        rotate(gc, 0, 0, 0);

        renderHealthBar(gc);
        renderExperienceBar(gc);
        renderManaBar(gc);

        renderPlayerLevel(gc);
    }

    public void renderHealthBar(GraphicsContext gc) {
        //Find percentage of health
        float currentHealth = (float) player.getCurrentHealth();
        float maxHealth = (float) player.getMaxHealth();
        float healthPercentage = currentHealth/maxHealth;

        //Render red bar based on percentage
        renderStatBar((int)(statBarHeight*2.5), 0, statBarWidth, statBarHeight, healthPercentage, Color.RED, gc);


    }

    public void renderManaBar(GraphicsContext gc) {
        //Find percentage of mana availab;e
        float currentMana = (float) player.getManaPoints();
        float maxMana = (float) player.getMaxMana();
        float manaPercentage = currentMana/maxMana;

        renderStatBar((int)(statBarHeight*2.5), statBarHeight, statBarWidth, statBarHeight, manaPercentage, Color.BLUE, gc);
    }

    public void renderExperienceBar(GraphicsContext gc) {
        //Find percentage of experience earned towards next level
        float currentExperience = (float) player.getExperience();
        float experienceAtLevelUp = currentExperience + (float) player.getExperienceToNextLevel();
        float experiencePercentage = currentExperience/experienceAtLevelUp;

        renderStatBar((int)(statBarHeight*2.5), statBarHeight*2, statBarWidth, statBarHeight/2, experiencePercentage, Color.GOLD, gc);
    }

    public void renderPlayerLevel(GraphicsContext gc) {
        int level = player.getLevel();

        //Render golden background behind number
        gc.setFill(Color.GOLDENROD);
        gc.fillRect(0, 0, (statBarHeight*2.5), (statBarHeight*2.5));


        gc.setFill(Color.BLACK);
        gc.setFont(Font.font ("Verdana", FontWeight.BOLD, 30));

        //Render level number
        gc.fillText(Integer.toString(level), 15, 35);
    }

    protected void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    //Renders a stat bar given a color and percent fill
    public void renderStatBar(int x, int y, int width, int height, float percentFill, Color fillColor, GraphicsContext gc) {
        gc.setFill(Color.GRAY);
        gc.fillRect(x, y, width, height);

        gc.setFill(fillColor);
        gc.fillRect(x, y, width*percentFill, height);
    }
}
