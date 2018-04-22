package Model.InfluenceEffect;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class LinearInfluenceEffect extends InfluenceEffect {


    public LinearInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    public LinearInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining, long nextMoveTime, Point3D originPoint) {
        super(command, range, speed, orientation, movesRemaining, nextMoveTime, originPoint);
    }

    // Defines logic for moving in a straight line in the direction of its orientation
    public ArrayList<Point3D> nextMove(Point3D point) {
        if(canMove()) {
            setNextMoveTime();

            ArrayList<Point3D> newPos = new ArrayList<>();

            if (rangeIsZero()) {
                newPos.add(point);
                return newPos;
            }

            Point3D newPoint = Orientation.getAdjacentPoint(point, getOrientation());
            setOriginPoint(newPoint);

            newPos.add(newPoint);
            decrementMovesRemaining();
            return newPos;
        }

        return new ArrayList<>();
    }

    public InfluenceEffect cloneInfluenceEffect() {
        return new LinearInfluenceEffect(getCommand(), getRange(), getSpeed(), getOrientation());
    }

    public void accept(Visitor visitor) {
        visitor.visitInfluenceEffect(this);
    }
}