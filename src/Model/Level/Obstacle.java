package Model.Level;

import Controller.Visitor.Visitable;
import Controller.Visitor.Visitor;

public class Obstacle implements Visitable {

    public Obstacle(){}

    public void accept(Visitor visitor) {
        visitor.visitObstacle(this);
    }
}
