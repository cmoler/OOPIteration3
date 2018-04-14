package Controller.ModelKeyAction;

import javafx.scene.input.KeyCode;

public abstract class ModelKeyAction {

    public ModelKeyAction(KeyCode keyCode){
        this.keyCode = keyCode;
    }

    KeyCode keyCode;

    public KeyCode getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(KeyCode keyCode) {
        this.keyCode = keyCode;
    }

    public abstract void handle(KeyCode incomingKey);

    public abstract String getName();
}
