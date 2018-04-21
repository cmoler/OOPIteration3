package Model.Command.EntityCommand.NonSettableCommand;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Command.LevelCommand.LevelCommand;
import Model.Entity.Entity;
import Model.Item.TakeableItem.TakeableItem;
import Model.Level.Level;
import Model.Level.LevelMessenger;
import javafx.geometry.Point3D;

public class DropItemCommand extends LevelCommand implements Command {

    private Entity entity;
    private TakeableItem item;

    public DropItemCommand(LevelMessenger levelMessenger) {
        super(levelMessenger);
    }

    public void receiveLevel(Level level) {
        level.dropItemFromEntity(entity, item);
    }

    public void execute(Entity entity) {
        this.entity = entity;

        sendCommandToLevel();
    }

    @Override
    public void accept(SavingVisitor savingVisitor) {
        savingVisitor.visitDropItemCommand(this);
    }

    public void setItem(TakeableItem item) {
        this.item = item;
    }

    public Entity getEntity() {
        return entity;
    }

    public TakeableItem getItem() {
        return item;
    }
}
