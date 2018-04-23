package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;

public class River implements Visitable {
    private Vec3d flowrate;
    private long nextMoveTime = 0;
    private long speed = 50000000l;

    public River(Vec3d flowrate) {
        this.flowrate = flowrate;
    }

    public Vec3d getPush(){
        if(canMove()) {
            setNextMoveTime();
            return flowrate;
        }
        return new Vec3d(0, 0,0 );
    }

    public Vec3d getFlowrate() {
        return flowrate;
    }

    public void setSpeed(long speed){
        this.speed = speed;
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

    private boolean canMove(){
        return System.nanoTime() > nextMoveTime;
    }

    private void setNextMoveTime() {
        nextMoveTime = System.nanoTime() + speed;
    }
}
