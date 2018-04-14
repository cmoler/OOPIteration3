package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.ModelKeyAction.ModelKeyAction;
import javafx.scene.input.KeyCode;

import java.util.List;

public class Controller {

    private List<ModelKeyAction> keyActionSet;
    private ControllerSetFactory controllerSetFactory;

    public Controller(){
        this.controllerSetFactory = new ControllerSetFactory(this);
    }

    public void triggerActionOnKeycode(KeyCode keyCode){
        for(int i = 0; i < keyActionSet.size(); ++i){
            keyActionSet.get(i).handle(keyCode);
        }
    }

    public void setKeyActionSet(List<ModelKeyAction> keyActionSet) {
        this.keyActionSet = keyActionSet;
    }
}
