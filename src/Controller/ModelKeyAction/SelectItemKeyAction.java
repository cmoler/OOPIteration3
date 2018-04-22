package Controller.ModelKeyAction;

import Model.Entity.Entity;
import javafx.scene.input.KeyCode;

public class SelectItemKeyAction extends ModelKeyAction {

    private Entity player;

    public SelectItemKeyAction(KeyCode keyCode, Entity player) {
        super(keyCode);
        this.player = player;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(incomingKey == keyCode){
            player.useItem();
        }
    }

    @Override
    public String getName() {
        return "selectItem";
    }
}
