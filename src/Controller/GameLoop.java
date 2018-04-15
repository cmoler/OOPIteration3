package Controller;

import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;

public class GameLoop {

    public void receiveGameLoopCommand(GameLoopCommand command) {
        command.receiveGameLoop(this);
    }

    public void openBarterWindow(Entity player, Entity npcTradingWith) {

    }

    public void openDialogWindow(Entity entity) {

    }

    public void createObservationWindow(Entity entity) {

    }
}
