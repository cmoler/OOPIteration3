package Model.InfluenceEffect;

import Controller.Visitor.Visitor;
import Model.Command.EntityCommand.SettableCommand.SettableCommand;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;

public class RadialInfluenceEffect extends InfluenceEffect{
    public RadialInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation) {
        super(command, range, speed, orientation);
    }

    public RadialInfluenceEffect(SettableCommand command, int range, long speed, Orientation orientation, int movesRemaining, long nextMoveTime, Point3D originPoint) {
        super(command, range, speed, orientation, movesRemaining, nextMoveTime, originPoint);
    }

    //Defines logic for moving in every direction
    public ArrayList<Point3D> nextMove(Point3D point) {
        if(canMove()) {
            setNextMoveTime();

            ArrayList<Point3D> newPoints = new ArrayList<>();

            if (rangeIsZero()) {
                newPoints.add(point);
                return newPoints;
            }

            int distance = getRange() - getMovesRemaining();

            Point3D newOriginPoint = Orientation.getAdjacentPoint(point, getOrientation());
            setOriginPoint(newOriginPoint);
            newPoints.add(newOriginPoint);

            int orientationIndex = getOrientation().getIndexOfOrientation(getOrientation()) + 2;
            int numTileSides = Orientation.values().length;

            Point3D ringPoint = newOriginPoint;

            for(int i = 0; i < numTileSides-1; i++) {
                Orientation ringTravelDirection = Orientation.values()[(orientationIndex) % 6];

                int ringTravelDistance = distance + 1;

                while(ringTravelDistance > 0) {
                    ringPoint = Orientation.getAdjacentPoint(ringPoint, ringTravelDirection);
                    newPoints.add(ringPoint);

                    ringTravelDistance--;
                }

                orientationIndex++;
            }

            decrementMovesRemaining();

            return newPoints;
        }
        return new ArrayList<>();
    }

    public InfluenceEffect cloneInfluenceEffect() {
        return new RadialInfluenceEffect(getCommand(), getRange(), getSpeed(), getOrientation());
    }

    public void accept(Visitor visitor) {
        visitor.visitInfluenceEffect(this);
    }
}