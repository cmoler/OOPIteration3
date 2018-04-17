package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.ModelKeyAction.ModelKeyAction;
import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import javafx.scene.input.KeyCode;

import java.util.List;

public class Controller {

    private List<ModelKeyAction> keyActionSet;
    private ControllerSetFactory controllerSetFactory;

    public Controller(GameLoop gameLoop){
        this.controllerSetFactory = new ControllerSetFactory(this, gameLoop);
    }

    public void triggerActionOnKeycode(KeyCode keyCode){
        for(int i = 0; i < keyActionSet.size(); ++i){
            keyActionSet.get(i).handle(keyCode);
        }
    }

    public void setKeyActionSet(List<ModelKeyAction> keyActionSet) {
        this.keyActionSet = keyActionSet;
    }

    public ControllerSetFactory getControllerSetFactory() {
        return controllerSetFactory;
    }

    public void createMenuSet(MenuModel menuModel){
        controllerSetFactory.createMenuSet(menuModel);
    }

    public void createTradeSet(MenuModel menuModel) {
        controllerSetFactory.createTradeSet(menuModel);
    }

    public void createPlayerControlsSet(Entity player, MenuModel menuModel){
        controllerSetFactory.createPlayerControlsSet(player, menuModel);
    }
}
