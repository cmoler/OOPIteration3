package Model.MenuModel;

import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Entity.EntityAttributes.SkillLevel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LevelUpMenu extends InGameMenuState {

    private HashMap<Skill, SkillLevel> playersSkills;

    public LevelUpMenu(MenuModel menuModel, Entity player) {
        super(menuModel, player);
        playersSkills = (HashMap<Skill, SkillLevel>) player.getSkillLevelsMap().clone();
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
            if (selectedUpDown < 0) selectedUpDown = 3;
            if (selectedUpDown > 3) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        if(selectedLeftRight == 0){
            menuSelection();
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
            if(i == selectedUpDown) {
                Skill skill = (Skill) pair.getKey();
                SkillLevel level = (SkillLevel) pair.getValue();
                playersSkills.put(skill, new SkillLevel(level.getSkillLevel() + 1));
                break;
            }
            i++;
        }
    }

    private void confirmSelection(){
        if(selectedLeftRight == 1) player.setSkillLevels(playersSkills);
    }

    private void menuSelection(){
        switch (selectedUpDown){
            case 0:
                menuModel.setActiveState(new InventoryMenu(menuModel, player));
                break;
            case 1:
                menuModel.setActiveState(new StatsMenu(menuModel, player));
                break;
            case 2:
                menuModel.setActiveState(new LevelUpMenu(menuModel, player));
                break;
            case 3:
                menuModel.setActiveState(new ExitGameMenu(menuModel, player));
                break;
        }
    }
}
