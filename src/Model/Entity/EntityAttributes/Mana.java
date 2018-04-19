package Model.Entity.EntityAttributes;

public class Mana {

    private int manaPoints;
    private int maxMana;

    public Mana() {
        manaPoints = 10;
        maxMana = 100;
    }

    public Mana(int manaPoints, int maxMana) {
        this.manaPoints = manaPoints;
        this.maxMana = maxMana;
    }

    public void increaseMana(int amount) {
        if(manaPoints + amount > maxMana) {
            manaPoints = maxMana;
        }

        else {
            manaPoints += amount;
        }
    }

    public void decreaseMana(int amount) {
        if(manaPoints - amount < 0) {
            manaPoints = 0;
        }

        else {
            manaPoints -= amount;
        }
    }

    public int getCurrentMana() {
        return manaPoints;
    }

    public int getMaxMana() {
        return maxMana;
    }
}
