package Model.Entity.EntityAttributes;

public class Gold {

    private int goldAmount;
    private int maxGold;

    public Gold() {
        goldAmount = 10;
        maxGold = 100;
    }

    public Gold(int goldAmount, int maxGold) {
        this.goldAmount = goldAmount;
        this.maxGold = maxGold;
    }

    public void increaseGold(int amount) {
        if(goldAmount + amount > maxGold) {
            goldAmount = maxGold;
        }

        else {
            goldAmount += amount;
        }
    }

    public void decreaseGold(int amount) {
        if(goldAmount - amount < 0) {
            goldAmount = 0;
        }

        else {
            goldAmount -= amount;
        }
    }
}
