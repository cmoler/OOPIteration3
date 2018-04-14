package Model.Level;

import Model.AI.AIController;
import Model.AI.AIState;
import Model.Command.GameModelCommand.GameModelCommand;
import Model.Entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameModel {

    private Level currentLevel;
    private List<Level> levels;
    private Map<Level, LevelMessenger> levelMessengers;
    private Entity player;
    private Map<Level, List<AIController>> aiMap;

    public void receiveGameModelCommand(GameModelCommand command) {
        command.receiveGameModel(this);
    }

    public AIController getAIForEntity(Entity entity) {
        ArrayList<AIController> ais = (ArrayList)aiMap.get(currentLevel);
        for(int i = 0; i < ais.size(); ++i){
            if(ais.get(i).getEntity() == entity){
                return ais.get(i);
            }
        }
        try {
            throw new Exception("couldnt find ai for that entity");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
