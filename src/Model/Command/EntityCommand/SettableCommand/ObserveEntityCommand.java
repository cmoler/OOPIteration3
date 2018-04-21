package Model.Command.EntityCommand.SettableCommand;

import Controller.GameLoop;
import Controller.Visitor.Visitor;
import Model.Command.GameLoopCommand.GameLoopCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class ObserveEntityCommand extends GameLoopCommand implements SettableCommand {

    private Entity observedEntity;
    private Point3D entityLocation;
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
            gameLoop.createObservationWindow(entityLocation, randomEntityFacts);
        }
    }

    public void receiveGameModel(GameModel gameModel) {
        if(gameModel.playerIsDead()) {
            playerIsNotDead = false;
        }
    }

    public void receiveLevel(Level level) {
        System.out.println("Received");
        // remove projectiles that fired this command so we only observe one entity, and don't execute this command a bunch of times
        level.removeInfluenceEffectsWithCommand(this);
        entityLocation = level.getEntityPoint(observedEntity);
    }

    public void execute(Entity entity) {
        this.observedEntity = entity;
        randomEntityFacts = observedEntity.getRandomFacts(observeAccuracy);
        System.out.println(randomEntityFacts);
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
