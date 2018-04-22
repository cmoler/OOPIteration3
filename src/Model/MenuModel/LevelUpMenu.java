package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Entity.EntityAttributes.SkillLevel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LevelUpMenu extends InGameMenuState {

    private HashMap<Skill, SkillLevel> playersSkills;
    private int pointsAvailable;

    public LevelUpMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
        playersSkills = (HashMap<Skill, SkillLevel>) player.getSkillLevelsMap().clone();
        this.pointsAvailable = player.getSkillPoints();
    }

    @Override
    public void correctParameters() {

        if(selectedUpDown < 0) selectedUpDown = playersSkills.size();
        if(selectedUpDown > playersSkills.size()) selectedUpDown = 0;
        if(selectedUpDown == 0){
            if (selectedLeftRight < 0) selectedLeftRight = 1;
            if (selectedLeftRight > 1) selectedLeftRight = 0;
        }
        if(selectedUpDown != 0) {
            if (selectedLeftRight < 0) selectedLeftRight = 2;
            if (selectedLeftRight > 2) selectedLeftRight = 0;
        }

        if(selectedLeftRight == 0) {
            if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
            if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
        }
    }

    public HashMap<Skill, SkillLevel> getPlayersSkills(){
        return playersSkills;
    }

    public int getPointsAvailable(){
        return pointsAvailable;
    }

    @Override
    public void select() {
        if(selectedLeftRight == 0){
            inGameMenuBar.select(selectedUpDown);
            return;
        }
        switch (selectedUpDown){
            case 0:
                confirmSelection();
                break;
            default:
                skillSelection();
                break;

        }
    }

    private void skillSelection(){
        Iterator it = playersSkills.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if(i == selectedUpDown - 1) {
                if (selectedLeftRight == 1) {
                    Skill skill = (Skill) pair.getKey();
                    SkillLevel level = (SkillLevel) pair.getValue();
                    SkillLevel newLevel = new SkillLevel(level.getSkillLevel());
                    if(newLevel.getSkillLevel() > 0) pointsAvailable++;
                    newLevel.decreaseSkillLevel();
                    playersSkills.put(skill, newLevel);
                }
                else if(selectedLeftRight == 2 && pointsAvailable > 0){
                    Skill skill = (Skill) pair.getKey();
                    SkillLevel level = (SkillLevel) pair.getValue();
                    SkillLevel newLevel = new SkillLevel(level.getSkillLevel());
                    if(!newLevel.isMaxLevel()) pointsAvailable--;
                    newLevel.increaseSkillLevel();
                    playersSkills.put(skill, newLevel);
                }
                break;
            }
            i++;
        }
    }

    private void confirmSelection(){
        if(selectedLeftRight == 1) {
            player.setSkillLevels(playersSkills);
            player.setSkillPointsAvaiable(pointsAvailable);
        }
    }

}
