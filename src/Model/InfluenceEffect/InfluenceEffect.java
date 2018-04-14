package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.Map;

public abstract class InfluenceEffect{
    private Command command;
    private int movesRemaining;
    private long nextMoveTime;
    private long speed;
    private Orientation orientation;
    private int range;

    private boolean hasNotFired;

    public InfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;
        this.range = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
        hasNotFired = true;



    }

    public ArrayList<Point3D> nextMove(Point3D point) {
        return new ArrayList<>();
    }

    //Passes entity into command
    public void hitEntity(Entity entity) {
        if(hasNotFired) {
            command.execute(entity);
            hasNotFired = false;
        }
    }


    public Orientation getOrientation() {
        return orientation;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public int getRange() {
        return range;
    }

    public long getSpeed() {
        return speed;
    }

    public void decrementMovesRemaining() {
        movesRemaining--;
    }

    public Command getCommand() {
        return command;
    }

    public abstract InfluenceEffect getClone();
}
