package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import Model.Entity.Entity;
import com.sun.javafx.geom.Vec3d;

public class River implements Visitable {
    private Vec3d flowrate;
    private int delay = 30;
    private int tick = 0;
    private int speed = 1;

    public River(Vec3d flowrate) {
        this.flowrate = flowrate;
    }

    public Vec3d getPush(){
        if(canMove()) {
            setNextMoveTime();
            return flowrate;
        }
        tick += speed;
        return new Vec3d(0, 0,0 );
    }

    public Vec3d getFlowrate() {
        return flowrate;
    }

    public void setSpeed(int speed){
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
        return tick > delay;
    }

    private void setNextMoveTime() {
        tick = 0;
    }
}
