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

    public void fire(Entity callingEntity) {
        // TODO: figure out how to get skills to modify stats for stuff like trap disarm or pickpocket, etc.
        influenceEffect.setOrientation(callingEntity.getOrientation());

        InfluenceEffect newInstance = influenceEffect.cloneInfluenceEffect();
        newInstance.setCommand(behavior);

        sendInfluenceEffectCommand.setInfluenceEffect(newInstance);
        sendInfluenceEffectCommand.execute(callingEntity);
    }

    public void setInfluence(InfluenceEffect influence) {
        this.influenceEffect = influence;
    }

    public void setBehavior(Command command) {
        behavior = command;
    }
}
