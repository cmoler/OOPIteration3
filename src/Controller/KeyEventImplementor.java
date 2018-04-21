package Controller;

import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class KeyEventImplementor implements EventHandler<KeyEvent> {

    Controller controller;

    public KeyEventImplementor(GameLoop gameLoop){
        this.controller = new Controller(gameLoop);
    }

    public void createInGameMenuSet(MenuModel menuModel){
        controller.createInGameMenuSet(menuModel);
    }

    public void createMainMenuSet(MenuModel menuModel){
        controller.createMenuSet(menuModel);
    }

    public void createPlayerControlsSet(Entity player, MenuModel menuModel){
        controller.createPlayerControlsSet(player, menuModel);
    }

    @Override
    public void handle(KeyEvent event) {
        controller.triggerActionOnKeycode(event.getCode());
    }
}
