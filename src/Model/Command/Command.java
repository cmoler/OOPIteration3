package Model.Command;

import Controller.Visitor.Visitable;
import Model.Entity.Entity;

public interface Command extends Visitable {
    void execute(Entity entity);
}
