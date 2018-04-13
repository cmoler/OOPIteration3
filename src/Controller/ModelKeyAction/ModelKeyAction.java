package Controller.ModelKeyAction;

import javafx.scene.input.KeyCode;

public abstract class ModelKeyAction {

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
