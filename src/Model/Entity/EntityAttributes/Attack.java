package Model.Entity.EntityAttributes;

public class Attack {

    private int attackPoints;
    private int modifier;

    public Attack() {
        attackPoints = 1;
        modifier = 0;
    }

    public Attack(int attackPoints, int modifier) {
        this.attackPoints = attackPoints;
        this.modifier = modifier;
    }

    public void increaseAttackPoints(int amount) {
        attackPoints += amount;
    }

    public void decreaseAttackPoints(int amount) {
        if(attackPoints - amount < 0) {
            attackPoints = 0;
        }

        else {
            attackPoints -= amount;
        }
    }

    public void addModifier() {
        attackPoints += modifier;
    }

    public void removeModifier() {
        attackPoints -= modifier;
    }

    public int getAttackPoints() {
        return attackPoints;
    }

    public int getModifier() {
        return modifier;
    }
}
