package Model.InfluenceEffect;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import View.LevelView.InfluenceEffectView;
import View.LevelView.LevelViewElement;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public abstract class InfluenceEffect implements Visitable {

    private LevelViewElement observer;
    private SettableCommand command;
    private int movesRemaining;
    private long nextMoveTime = 0;
    private long speed;
    private Orientation orientation;
    private int range;
    private Point3D originPoint;

    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;
        this.range = range;
        this.speed = speed;
        this.orientation = orientation;
    }

    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining, long nextMoveTime, Point3D originPoint) {
        this.command = command;
        this.movesRemaining = movesRemaining;
        this.range = range;
        this.speed = speed;
        this.orientation = orientation;
        this.nextMoveTime = nextMoveTime;
        this.originPoint = originPoint;
    }

    public abstract ArrayList<Point3D> nextMove(Point3D point);

    public abstract InfluenceEffect cloneInfluenceEffect();

    public void setOriginPoint(Point3D originPoint) {
        this.originPoint = originPoint;
    }

    public Point3D getOriginPoint() {
        return originPoint;
    }

    protected boolean movingAtOrigin(Point3D point) {
        return  (originPoint.getX() == point.getX()) &&
                (originPoint.getY() == point.getY()) &&
                (originPoint.getZ() == point.getZ());

    }

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

    protected long getNextMoveTime() {
        return nextMoveTime;
    }

    public boolean noMovesRemaining() {
        return movesRemaining < 0;
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

    public LevelViewElement getObserver() {
        return observer;
    }

    protected boolean canMove() {
        return System.nanoTime() > nextMoveTime;
    }

    protected void setNextMoveTime() {
        nextMoveTime = System.nanoTime() + speed;
    }

    public void setObserver(InfluenceEffectView influenceEffectView) {
        this.observer = influenceEffectView;
    }
}