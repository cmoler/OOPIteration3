/*package Model.Command.EntityCommand.SettableCommand;

import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.Command.Command;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;

public class ChangeToHostileAICommand extends GameModelCommand implements Command {
    private Entity targetEnt;
    private HostileAI hostileAI;

    public ChangeToHostileAICommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void execute(Entity entity) { // Called First
        targetEnt = entity;
    }

    @Override
    public void receiveGameModel(GameModel gameModel) { // Get what I need to get from the receiving Game Model - Called Last
        AIController AIChanger = gameModel.getAIForEntity(targetEnt);
        AIChanger.setActiveState(new HostileAI());

    }

    @Override
    public void receiveLevel(Level level) { // What I need to get from the receiving Level - Called Second
        hostileAI = new HostileAI(targetEnt,level.getTerrainLocations(),level.getEntityLocations(),level.getObstacleLocations(),);
    }
}*/
