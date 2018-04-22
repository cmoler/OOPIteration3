package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;

public class River implements Visitable {
    private Vec3d flowrate;

    public River(Vec3d flowrate) {
        this.flowrate = flowrate;
    }

    public Vec3d getFlowrate() {
        return flowrate;
    }

    public void setFlowrate(Vec3d flowrate) {
        this.flowrate = flowrate;
    }

    public void accept(Visitor visitor) {
        visitor.visitRiver(this);
    }

    public boolean isTraversable(Entity e) {
        return e.getTraversalStrength() > flowrate.length();
    }
}
