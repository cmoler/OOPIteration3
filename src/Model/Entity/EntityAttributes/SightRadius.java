package Model.Entity.EntityAttributes;

public class SightRadius {

    private int sight;

    public SightRadius() {
        sight = 1;
    }

    public SightRadius(int sight) {
        this.sight = sight;
    }

    public void increaseSight(int amount) {
        sight += amount;
    }

    public void decreaseSight(int amount) {
        if(sight - amount < 0) {
            sight = 0;
        }

        else {
            sight -= amount;
        }
    }

    public int getSight() {
        return sight;
    }

    public void setSight(int sight) {
        this.sight = sight;
    }
}
