package Model.Entity.EntityAttributes;

public class Defense {

    private int defensePoints;
    private int modifier;

    public Defense() {
        defensePoints = 1;
        modifier = 0;
    }

    public Defense(int defensePoints, int modifier) {
        this.defensePoints = defensePoints;
        this.modifier = modifier;
    }

    public void increaseDefensePoints(int amount) {
        defensePoints += amount;
    }

    public void decreaseDefensePoints(int amount) {
        if(defensePoints - amount < 0) {
            defensePoints = 0;
        }

        else {
            defensePoints -= amount;
        }
    }

    public void addModifier() {
        defensePoints += modifier;
    }

    public void removeModifier() {
        defensePoints -= modifier;
    }

    public int getDefensePoints() {
        return defensePoints;
    }

    public int getModifier() {
        return modifier;
    }
}
