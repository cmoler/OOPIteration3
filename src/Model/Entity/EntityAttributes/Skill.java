package Model.Entity.EntityAttributes;

import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;

public class Skill {

    private String name;
    private InfluenceEffect influenceEffect;
    private SettableCommand behavior;
    private SendInfluenceEffectCommand sendInfluenceEffectCommand;
    private int accuracy;
    private int useCost;

    public Skill() {
        name = "NEED NAME HERE";
        influenceEffect = new LinearInfluenceEffect(new RemoveHealthCommand(5), 5, 5, Orientation.NORTH);
        behavior = null;
        accuracy = 0;
        useCost = 0;
    }

    public Skill(String name, InfluenceEffect influenceEffect, SettableCommand behavior, SendInfluenceEffectCommand sendInfluenceEffectCommand,
                 int accuracy, int useCost) {
        this.name = name;
        this.influenceEffect = influenceEffect;
        this.behavior = behavior;
        this.sendInfluenceEffectCommand = sendInfluenceEffectCommand;
        this.accuracy = accuracy;
        this.useCost = useCost;
    }

    public void fire(Entity callingEntity) {
        influenceEffect.setOrientation(callingEntity.getOrientation());

        // for each skill level, get behaviors current value, add 10 to it
        int skillLevel = callingEntity.getSkillLevel(this);

        int behaviorAmount = behavior.getAmount();
        behaviorAmount = behaviorAmount + skillLevel * 10;

        behavior.setAmount(behaviorAmount);

        InfluenceEffect newInstance = influenceEffect.cloneInfluenceEffect();
        newInstance.setCommand(behavior);

        sendInfluenceEffectCommand.setInfluenceEffect(newInstance);
        sendInfluenceEffectCommand.execute(callingEntity);
    }

    public void setInfluence(InfluenceEffect influence) {
        this.influenceEffect = influence;
    }

    public void setBehavior(SettableCommand command) {
        behavior = command;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getUseCost() {
        return useCost;
    }

    public InfluenceEffect getInfluenceEffect() {
        return influenceEffect;
    }

    public SettableCommand getBehavior() {
        return behavior;
    }

    public SendInfluenceEffectCommand getSendInfluenceEffectCommand() {
        return sendInfluenceEffectCommand;
    }

    public void setSendInfluenceEffectCommand(SendInfluenceEffectCommand sendInfluenceEffectCommand) {
        this.sendInfluenceEffectCommand = sendInfluenceEffectCommand;
    }

    public String getName(){
        return name;
    }
}
