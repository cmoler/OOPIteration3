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
    private boolean replacedKeySet = false;

    public Controller(GameLoop gameLoop){
        this.controllerSetFactory = new ControllerSetFactory(this, gameLoop);
    }

    public void triggerActionOnKeycode(KeyCode keyCode){
        for(int i = 0; i < keyActionSet.size(); ++i){
            if(replacedKeySet) break;
            keyActionSet.get(i).handle(keyCode);
        }
        replacedKeySet = false;
    }

    public void setKeyActionSet(List<ModelKeyAction> keyActionSet) {
        this.keyActionSet = keyActionSet;
        replacedKeySet = true;
    }

    public ControllerSetFactory getControllerSetFactory() {
        return controllerSetFactory;
    }

    public void createInGameMenuSet(MenuModel menuModel){
        controllerSetFactory.createInGameMenuSet(menuModel);
    }

    public void createMenuSet(MenuModel menuModel){
        controllerSetFactory.createMenuSet(menuModel);
    }


    public void createPlayerControlsSet(Entity player, MenuModel menuModel){
        controllerSetFactory.createPlayerControlsSet(player, menuModel);
    }
}
