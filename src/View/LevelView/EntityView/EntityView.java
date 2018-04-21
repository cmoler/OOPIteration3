package View.LevelView.EntityView;

import Model.Entity.Entity;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

public abstract class EntityView extends LevelViewElement {

    private Entity entity;

    public EntityView(Entity entity, Point3D location) {
        super(location, 1);
        this.entity = entity;
        entity.setObserver(this);
        setOrientation(entity.getOrientation());
    }

    public void notifyViewElement() {
        setOrientation(entity.getOrientation());
    }
}