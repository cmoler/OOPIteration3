package Model.Command.EntityCommand.SettableCommand;

import Controller.Visitor.Visitor;
import Model.AI.AIController;
import Model.AI.HostileAI;
import Model.Command.Command;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public class ChangeToHostileAICommand extends GameModelCommand implements Command {
    private Entity aggroEnt;
    private Entity attacker;
    private HostileAI hostileAI;

    public ChangeToHostileAICommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    @Override
    public void execute(Entity entity) { // Called First
        attacker = entity;
    }

    @Override
    public void receiveGameModel(GameModel gameModel) { // Get what I need to get from the receiving Game Model - Called Last
        AIController AIChanger = gameModel.getAIForEntity(aggroEnt);
        AIChanger.setActiveState(hostileAI);
    }

    @Override
    public void receiveLevel(Level level) { // What I need to get from the receiving Level - Called Second
        Point3D aggroPoint = Orientation.getAdjacentPoint(level.getEntityPoint(attacker),attacker.getOrientation());
        List<Entity> targetList = new ArrayList<>();
        targetList.add(level.getEntityAtPoint(aggroPoint));
        hostileAI = new HostileAI(aggroEnt,level.getTerrainMap(),level.getEntityMap(),level.getObstacleMap(),targetList);
    }

    public void accept(Visitor visitor) {
        //TODO: Implement This
    }
}
