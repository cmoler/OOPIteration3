package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class LockMovementKeyAction extends ModelKeyAction {

    private Entity player;
    private boolean locked;

    public LockMovementKeyAction(KeyCode keyCode, Entity player) {
        super(keyCode);
        this.player = player;
        this.locked = false;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            if(!locked) {
                player.setMoveable(false);
                locked = !locked;
            }
            else{
                player.setMoveable(true);
                locked = !locked;
            }
        }
    }

    @Override
    public String getName() {
        return null;
    }
}
