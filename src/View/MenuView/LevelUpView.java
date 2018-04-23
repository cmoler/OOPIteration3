package View.MenuView;

import Configs.Commons;
import Configs.TextBoxInfo;
import Model.Entity.EntityAttributes.Skill;
import Model.Entity.EntityAttributes.SkillLevel;
import Model.Level.Level;
import Model.MenuModel.LevelUpMenu;
import Model.MenuModel.MenuModel;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LevelUpView extends InGameMenuView {

    private Image selected;

    public LevelUpView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLACK_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {
        renderConfirm(gc);
        renderSkills(gc);
    }

    private void renderConfirm(GraphicsContext gc) {
        int selectedX = menuModel.getSelectedHorizontal();
        int selectedY = menuModel.getSelectedVertical();

        int startX = 3 * Commons.SCREEN_WIDTH / 7;
        int startY = Commons.SCREEN_HEIGHT / 30;
        int width = Commons.SCREEN_WIDTH / 5;
        int height = Commons.SCREEN_HEIGHT / 16;

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(startX, startY, width, height);

        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);
        gc.fillText("Confirm", startX + width / 4, startY+4*height/5);

        String pointsAvailble = Integer.toString(((LevelUpMenu)menuModel.getActiveState()).getPointsAvailable());
        gc.fillText("Points available: " + pointsAvailble, startX, startY+ 12*height/5);

        gc.strokeRect(startX, startY, width, height);

        if(selectedY == 0 && selectedX == 1)
            gc.drawImage(selected, startX, startY, width, height);
    }

    private void renderSkills(GraphicsContext gc) {
        int startX = 3 * Commons.SCREEN_WIDTH / 8;
        int startY = Commons.SCREEN_HEIGHT / 5;

        HashMap<Skill, SkillLevel> playersSkills = ((LevelUpMenu)menuModel.getActiveState()).getPlayersSkills();

        int width = Commons.SCREEN_WIDTH/8;
        int height;
        if(playersSkills.size() == 0) height = 0;
        else height = 40 * Commons.SCREEN_HEIGHT / 60 / playersSkills.size();

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(startX, startY, 8 * width / 3, height * playersSkills.size());

        gc.setFont(new Font(30.0f));
        gc.setFill(Color.WHITESMOKE);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5.0f);
        Iterator it = playersSkills.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Skill skill = (Skill) pair.getKey();
            SkillLevel level = (SkillLevel) pair.getValue();

            gc.fillText("-", startX + width / 7, startY + i * height+3*height/5);
            gc.fillText(skill.getName()+":", startX+width / 2, startY + i * height+3*height/5, 4 * width / 3);
            gc.fillText(Integer.toString(level.getSkillLevel()), startX + 2 * width, startY + i * height+3*height/5);
            gc.fillText("+", startX + 2 * width + 3 * width / 7, startY + i * height+3*height/5);

            gc.strokeRect(startX, startY + i * height, width / 3, height);
            gc.strokeRect(startX + width / 3, startY + i * height, 6 * width / 3, height);
            gc.strokeRect(startX + 7 * width / 3, startY + i * height, width / 3, height);

            i++;
        }

        int selectedX = menuModel.getSelectedHorizontal();
        int selectedY = menuModel.getSelectedVertical() - 1;

        int selectionBoxY = startY + selectedY * height;

        if(selectedY != -1 && selectedX == 1)
            gc.drawImage(selected, startX, selectionBoxY, width / 3, height);

        if(selectedY != -1 && selectedX == 2)
            gc.drawImage(selected, startX + 7 * width / 3, selectionBoxY, width / 3, height);

    }
}
