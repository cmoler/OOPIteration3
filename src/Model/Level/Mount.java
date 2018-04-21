//TODO: Add Default Constructor and see if Observers are needed.*/

package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;
import View.LevelView.LevelViewElement;
import Model.Entity.EntityAttributes.Speed;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;

import java.util.ArrayList;
import java.util.List;

public class Mount implements Visitable {
    private Orientation orientation;
    private Speed movementSpeed;
    private List<Terrain> passableTerrain;
    private List<LevelViewElement> observers;

    public Mount(){
        movementSpeed = new Speed();
        passableTerrain = new ArrayList<>();
        observers = new ArrayList<>();
        passableTerrain.add(Terrain.GRASS);
        orientation = Orientation.NORTH;

        movementSpeed.setSpeed(1);
    }

    public Mount(Orientation orientation, Speed movementSpeed, List<Terrain> passableTerrain, List<LevelViewElement> observers) {
        this.orientation = orientation;
        this.movementSpeed = movementSpeed;
        this.passableTerrain = passableTerrain;
        this.observers = observers;
    }

    public Speed getMovementSpeed() {
        return movementSpeed;
    }

    public String speedToString() {
        return Integer.toString(movementSpeed.getSpeed());
    }

    public void setMovementSpeed(Speed movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public List<Terrain> getPassableTerrain() {
        return passableTerrain;
    }

    public void setPassableTerrain(List<Terrain> passableTerrain) {
        this.passableTerrain = passableTerrain;
    }

    public List<LevelViewElement> getObservers() {
        return observers;
    }

    public  void addObserver(LevelViewElement observer) {
        observers.add(observer);
    }

    public void setObservers(List<LevelViewElement> observers) {
        this.observers = observers;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void notifyObservers(Point3D point){
        for (LevelViewElement o : observers) {
            o.notifyViewElement();
            if(point != null) { o.setPosition(point); }
        }
    }

    public void accept(Visitor visitor) {
        visitor.visitMount(this);
    }
}
