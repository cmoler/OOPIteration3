package Model.Command.EntityCommand.SettableCommand;

import Controller.GameLoop;
import Controller.Visitor.Visitor;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class ObserveEntityCommand extends GameLoopCommand implements SettableCommand {

    private Entity observedEntity;
    private String randomEntityFacts;

    int observeAccuracy;
    boolean playerIsNotDead;

    public ObserveEntityCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
        randomEntityFacts = "Nothing to report!";
        playerIsNotDead = true;
    }

    public void receiveGameLoop(GameLoop gameLoop) {
        if (playerIsNotDead) {
            gameLoop.createObservationWindow(randomEntityFacts);
        }
    }

    public void receiveGameModel(GameModel gameModel) {
        if(gameModel.playerIsDead()) {
            playerIsNotDead = false;
        }
    }

    public void receiveLevel(Level level) {
        // remove projectiles that fired this command so we only observe one entity, and don't execute this command a bunch of times
        level.removeInfluenceEffectsWithCommand(this);
    }

    public void execute(Entity entity) {
        this.observedEntity = entity;
        randomEntityFacts = observedEntity.getRandomFacts(observeAccuracy);
        sendCommandToGameLoop();
    }

    public void setAmount(int amount) {
        this.observeAccuracy = amount;
    }

    public int getAmount() {
        return observeAccuracy;
    }

    public void accept(Visitor visitor) {
        visitor.visitObserveEntityCommand(this);
    }
}
