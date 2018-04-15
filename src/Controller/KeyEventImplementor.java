package Controller;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyEventImplementor implements EventHandler<KeyEvent> {

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

    @Override
    public void handle(KeyEvent event) {
        controller.triggerActionOnKeycode(event.getCode());
    }
}
