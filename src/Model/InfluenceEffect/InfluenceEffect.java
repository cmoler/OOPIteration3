package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.Map;

public class InfluenceEffect {
    private Command command;
    private int movesRemaining;
    private long nextMoveTime;
    private long speed;
    private Orientation orientation;

    private boolean hasNotFired;

    public InfluenceEffect(Command command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
        hasNotFired = true;



    }

    public void nextMove(Map<Point3D, InfluenceEffect> effectMap) {

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



    public void decrementMovesRemaining() {
        movesRemaining--;
    }
}
