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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LevelUpView extends InGameMenuView {

    private int selectedY;
    private int selectedX;
    private Image selected;

    public LevelUpView(MenuModel menuModel) {
        super(menuModel);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/BLUE_AOE.png");
        selected = new Image(file.toURI().toString());
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {

        gc.clearRect(0, 0, Commons.SCREEN_WIDTH, Commons.SCREEN_HEIGHT);

        int startX = Commons.SCREEN_WIDTH / 4;;
        int startY = Commons.SCREEN_WIDTH / 8;

        HashMap<Skill, SkillLevel> playersSkills = ((LevelUpMenu)menuModel.getActiveState()).getPlayersSkills();

        int width = Commons.SCREEN_WIDTH/8;
        int height = Commons.SCREEN_HEIGHT / playersSkills.size();

        Iterator it = playersSkills.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Skill skill = (Skill) pair.getKey();
            SkillLevel level = (SkillLevel) pair.getValue();

            gc.fillText("-", startX, startY + i * height);
            gc.fillText(skill.getName(), startX+width, startY + i * height);
            gc.fillText(Integer.toString(level.getSkillLevel()), startX + 2 * width, startY + i * height);
            gc.fillText("+", startX + 3 * width, startY + i * height);

            i++;
        }

        selectedX = menuModel.getSelectedHorizontal();
        selectedY = menuModel.getSelectedVertical();

        int selectionBoxX = startX + width * selectedX;
        int selectionBoxY = startY + selectedY * height;

        if(selectedX != 0)
        gc.drawImage(selected, selectionBoxX, selectionBoxY, TextBoxInfo.TEXTBOX_WIDTH, TextBoxInfo.TEXTBOX_HEIGHT);
    }
}
