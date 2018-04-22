package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.Visitor;
import Model.Command.Command;
import Model.Command.LevelCommand.LevelCommand;
import Model.Entity.Entity;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class SendInfluenceEffectCommand extends LevelCommand implements Command {

    private InfluenceEffect influenceEffect;
    private Entity entity;

    public SendInfluenceEffectCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void receiveLevel(Level level) {
        Point3D entityPoint = level.getEntityPoint(entity);
        if(entityPoint != null) {
            influenceEffect.setOriginPoint(entityPoint);
            level.addInfluenceEffectTo(entityPoint, influenceEffect);
        }
    }

    public void execute(Entity entity) {
        this.entity = entity;
        sendCommandToLevel();
    }

    public void setInfluenceEffect(InfluenceEffect influenceEffect) {
        this.influenceEffect = influenceEffect.cloneInfluenceEffect();
    }

    public InfluenceEffect getInfluenceEffect() {
        return influenceEffect;
    }

    public Entity getEntity() {
        return entity;
    }

    public void accept(Visitor visitor) {
        visitor.visitSendInfluenceEffectCommand(this);
    }
}
