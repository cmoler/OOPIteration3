package Model.Entity.EntityAttributes;

public class XPLevel {

    private int level;
    private int experience;
    private int expToNextLevel; // TODO: according to the UML, this is supposed to be derived?

    public XPLevel() {
        this.level = 0;
        this.experience = 0;
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
    }

    public void increaseExperience(int amt) {
        this.experience += amt;
    }
}
