package Model.Condition;

import Controller.Visitor.Visitable;
import Model.Entity.Entity;

public interface Condition extends Visitable {
    boolean checkCondition(Entity entity);
}
