package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.FriendlyAI;
import Model.Command.Command;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class ChangeToFriendlyAICommand extends GameModelCommand implements Command {
    private Entity changedEntity;
    private FriendlyAI friendlyAI;

    public ChangeToFriendlyAICommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void execute(Entity entity) { // Called First
        changedEntity = entity;
    }

    @Override
    public void receiveGameModel(GameModel gameModel) { // Get what I need to get from the receiving Game Model - Called Last
        AIController AIChanger = gameModel.getAIForEntity(changedEntity);
        AIChanger.setActiveState(friendlyAI);
    }

    @Override
    public void receiveLevel(Level level) { // What I need to get from the receiving Level - Called Second
        friendlyAI = new FriendlyAI(changedEntity,level.getTerrainMap(),level.getEntityMap(),level.getObstacleMap(), level.getRiverMap());
    }


    public void accept(Visitor visitor) {
        //TODO: Implement This
    }
}
