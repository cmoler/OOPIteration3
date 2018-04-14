package Model.Entity.EntityAttributes;

import Model.Command.Command;
import Model.Command.LevelCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;

public class Skill {

    private String name;
    private InfluenceEffect influenceEffect;
    private Command behavior;
    private SendInfluenceEffectCommand sendInfluenceEffectCommand;
    private int accuracy;
    private int useCost;

    public Skill() {
        name = "NEED NAME HERE";
        influenceEffect = null;
        behavior = null;
        accuracy = 0;
        useCost = 0;
    }

    public Skill(String name, InfluenceEffect influenceEffect, Command behavior, SendInfluenceEffectCommand sendInfluenceEffectCommand,
                 int accuracy, int useCost) {
        this.name = name;
        this.influenceEffect = influenceEffect;
        this.behavior = behavior;
        this.sendInfluenceEffectCommand = sendInfluenceEffectCommand;
        this.accuracy = accuracy;
        this.useCost = useCost;
    }

    public void fire(Entity entity) {
        // TODO: write in logic once send influence command is finished
        sendInfluenceEffectCommand.setInfluenceEffect(influenceEffect);
        sendInfluenceEffectCommand.execute(entity);
        sendInfluenceEffectCommand.sendCommandToLevel();
    }
}
