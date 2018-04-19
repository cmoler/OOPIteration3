package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class DismountKeyAction extends ModelKeyAction {

    private Entity player;

    public DismountKeyAction(KeyCode keyCode, Entity player) {
        super(keyCode);
        this.player = player;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if (incomingKey == keyCode){
            player.dismountVehicle();
        }
    }

    @Override
    public String getName() {
        return "dismount";
    }
}
