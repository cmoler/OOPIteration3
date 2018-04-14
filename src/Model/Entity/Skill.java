package Model.Entity;

import Model.Command.Command;
import Model.Command.LevelCommand.SendInfluenceEffectCommand;
import Model.InfluenceEffect.InfluenceEffect;

public class Skill {

    private String name;
    private InfluenceEffect influenceEffect;
    private Command behavior;
    private SendInfluenceEffectCommand sendInfluenceEffectCommand;
    private int accuracy;
    private int useCost;
}
