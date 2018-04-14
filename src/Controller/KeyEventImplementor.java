package Controller;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;

public class KeyEventImplementor {

    Controller controller;

    public KeyEventImplementor(){
        this.controller = new Controller();
    }

    public void createTradeSet(MenuModel menuModel){
        controller.createTradeSet(menuModel);
    }

    public void createPlayerControlsSet(Entity player){
        controller.createPlayerControlsSet(player);
    }
}
