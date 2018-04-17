package Model.InfluenceEffect;

import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public abstract class InfluenceEffect{
    private SettableCommand command;
    private int movesRemaining;
    private long nextMoveTime;
    private long speed;
    private Orientation orientation;
    private int range;


    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;
        this.range = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
    }

    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining) {
        this.command = command;
        this.movesRemaining = movesRemaining;
        this.range = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
    }

    public abstract ArrayList<Point3D> nextMove(Point3D point);

    public abstract InfluenceEffect cloneInfluenceEffect();

    public void hitEntity(Entity entity) {
        command.execute(entity);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public int getMovesRemaining() {
        return movesRemaining;
    }

    public boolean noMovesRemaining() {
        return movesRemaining == 0;
    }

    public int getRange() {
        return range;
    }

    public boolean rangeIsZero() {
        return range == 0;
    }

    public long getSpeed() {
        return speed;
    }

    public void decrementMovesRemaining() {
        movesRemaining--;
    }

    public void decreaseCommandAmount() {
        // for each distance travelled, decrease command's strength by 5

        int commandAmount = command.getAmount();

        commandAmount -= 5;

        if(commandAmount < 0) {
            commandAmount = 0;
        }

        command.setAmount(commandAmount);
    }

    public SettableCommand getCommand() {
        return command;
    }

    public void setCommand(SettableCommand command) {
        this.command = command;
    }
}