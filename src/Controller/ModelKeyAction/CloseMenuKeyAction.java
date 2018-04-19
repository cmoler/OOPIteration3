package Controller.ModelKeyAction;

import Controller.GameLoop;
import javafx.scene.input.KeyCode;

import javax.swing.event.MenuKeyEvent;

public class CloseMenuKeyAction extends ModelKeyAction {

    private GameLoop gameLoop;

    public CloseMenuKeyAction(KeyCode keyCode, GameLoop gameLoop) {
        super(keyCode);
        this.gameLoop = gameLoop;
    }

    @Override
    public void handle(KeyCode incomingKey) {
        if(keyCode == incomingKey){
            gameLoop.closeMenu();
        }
    }

    @Override
    public String getName() {
        return "closeMenu";
    }
}
