package Controller.ModelKeyAction;

import Controller.Factories.ControllerSetFactory;
import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class ToggleLockViewPortKeyAction extends ModelKeyAction {

    private Entity entity;
    private ControllerSetFactory controllerSetFactory;
    private boolean controllingPlayer;

    public ToggleLockViewPortKeyAction(KeyCode keyCode, Entity entity, ControllerSetFactory controllerSetFactory, boolean controllingPlayer) {
        super(keyCode);
        this.entity = entity;
        this.controllerSetFactory = controllerSetFactory;
        this.controllingPlayer = controllingPlayer;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(controllingPlayer){
                controllerSetFactory.createScrollingViewPortSet(entity);
            }
            else{
                controllerSetFactory.createPlayerControlsSet(entity);
            }
        }
    }

    @Override
    public String getName() {
        return "toggleLockView";
    }
}
