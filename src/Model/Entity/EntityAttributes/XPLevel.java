package Model.Entity.EntityAttributes;

public class XPLevel {

    private int level;
    private int experience;
    private int pointsAvailable;
    private int expToNextLevel; // TODO: according to the UML, this is supposed to be derived?

    public XPLevel() {
        this.level = 1;
        this.experience = 0;
        this.pointsAvailable = 0;
        this.expToNextLevel = 100; // TODO: define more complex algorithm for increasing level and managing experience
    }

    public XPLevel(int level, int experience, int expToNextLevel) {
        this.level = level;
        this.experience = experience;
        this.expToNextLevel = expToNextLevel;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getExpToNextLevel() {
        return expToNextLevel;
    }

    public void increaseLevel() {
        level++;
        pointsAvailable += 4;
    }

    public int getPointsAvailable() {
        return pointsAvailable;
    }

    public boolean pointsAvailable(){
        return pointsAvailable > 0;
    }

    public void increaseExperience(int amt) {
        this.experience += amt;
    }

    public void setPointsAvailable(int amount) {
        this.pointsAvailable = amount;
    }
}
