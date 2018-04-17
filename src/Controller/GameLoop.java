package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.Factories.EntityFactory;
import Controller.Visitor.SavingVisitor;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.MenuModel.MenuModel;
import javafx.animation.AnimationTimer;

public class GameLoop {

    private AnimationTimer loopTimer;
    private GameModel gameModel;
    private MenuModel menuModel;
    private SavingVisitor gameSaver;
    private EntityFactory entityFactory;
    private ControllerSetFactory controllerSetFactory;
    private KeyEventImplementor controls;

    public GameLoop() {
        //TODO: Add loading logic
        controls = new KeyEventImplementor(this);
        loopTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                gameModel.advance();
            }
        };
    }

    public void openBarterWindow(Entity playerEntity, int playerBarterStrength, Entity receivingEntity) {
        // TODO: implement
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }
    }

    public void openDialogWindow(Entity playerEntity, Entity receivingEntity) {
        // TODO: implement
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }
    }

    public void createObservationWindow(String randomEntityFacts) {
        // TODO: implement
    }

    public void startTimer() {
        loopTimer.start();
    }

    public void pauseTimer() {
        loopTimer.stop();
    }

    public void loadGame(int i) {

    }

    public void saveGame(int i) {

    }

    public void newGame(int i) {

    }

    public KeyEventImplementor getControls() {
        return controls;
    }
}
