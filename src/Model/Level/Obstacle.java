package Model.Level;

import Controller.Visitor.SavingVisitor;
import Controller.Visitor.Visitable;

public class Obstacle implements Visitable {

    public Obstacle(){}

    public void accept(SavingVisitor visitor) {
        visitor.visitObstacle(this);
    }
}
