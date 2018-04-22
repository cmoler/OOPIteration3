package Model.InfluenceEffect;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;
import Model.Command.Command;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import View.LevelView.InfluenceEffectView;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public abstract class InfluenceEffect implements Visitable {
    private SettableCommand command;
    private int movesRemaining;
    private long nextMoveTime;
    private long speed;
    private Orientation orientation;
    private int range;
    private List<InfluenceEffectView> influenceEffectViews;
    protected int count;
    private long lastFireTime;
    private boolean isStartPoint;


    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        this.command = command;
        this.movesRemaining = range;
        this.range = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
        count = 0;
        influenceEffectViews = new ArrayList<>();
        this.speed = 2000000000/speed;
        lastFireTime = System.nanoTime();
        isStartPoint = true;
    }

    public InfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining) {
        this.command = command;
        this.movesRemaining = movesRemaining;
        this.range = range;

        this.speed = speed;
        //TODO: make nextMoveTime based on speed
        this.orientation = orientation;
        influenceEffectViews = new ArrayList<>();
        lastFireTime = System.nanoTime();
        isStartPoint = true;
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

    public void addInfluenceEffectView(InfluenceEffectView influenceEffectView) {
        if(influenceEffectViews == null) {
            System.out.println("null");
        }
        influenceEffectViews.add(influenceEffectView);
    }
    public List<InfluenceEffectView> getInfluenceEffectViews() {
        return influenceEffectViews;
    }
    public void clearInfluenceEffectViews() {
        influenceEffectViews.clear();
    }

    public boolean readyToMove() {
        if(System.nanoTime()-lastFireTime > speed) {
            lastFireTime = System.nanoTime();
            return true;
        }
        return false;




    }

    public boolean isStartPoint() {
        return isStartPoint;
    }

    public void setIsStartPoint(boolean startPoint) {
        isStartPoint = startPoint;
    }
}