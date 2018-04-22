package Model.MenuModel;

import Controller.GameLoop;
import Model.Entity.Entity;

public class StatsMenu extends InGameMenuState {

    public StatsMenu(MenuModel menuModel, Entity player, GameLoop gameLoop) {
        super(menuModel, player, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = 3;
        if(selectedLeftRight > 3) selectedLeftRight = 0;
        if(selectedLeftRight == 0) {
            if (selectedUpDown < 0) selectedUpDown = inGameMenuBar.getMaxUp();
            if (selectedUpDown > inGameMenuBar.getMaxUp()) selectedUpDown = 0;
        }
    }

    @Override
    public void select() {
        if(selectedLeftRight == 0){
            inGameMenuBar.select(selectedUpDown);
            return;
        }
        switch (selectedLeftRight){
            case 1:
                player.unequipWeapon();
                break;
            case 2:
                player.unequipArmor();
                break;
            case 3:
                player.unequipRing();
        }
    }

    public Entity getPlayer(){
        return player;
    }
}
