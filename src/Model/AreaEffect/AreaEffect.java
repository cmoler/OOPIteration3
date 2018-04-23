package Model.AreaEffect;

import Controller.Visitor.Visitable;
import Model.Entity.Entity;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

public interface AreaEffect extends Visitable {

    void trigger(Entity entity);

    void notifyObserver(Point3D point3D);

    LevelViewElement getObserver();
}
