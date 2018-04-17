package Model.AreaEffect;

import Controller.Visitor.Visitable;
import Model.Entity.Entity;

public interface AreaEffect extends Visitable {

    void trigger(Entity entity);
}
