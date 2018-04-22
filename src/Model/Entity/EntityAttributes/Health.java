package Model.Entity.EntityAttributes;

public class Health {

    private int currentHealth;
    private int maxHealth;

    public Health() {
        currentHealth = 100;
        maxHealth = 100;
    }

    public Health(int currentHealth, int maxHealth)
    {
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
    }

    public void increaseMaxHealth(int amount) {
        this.maxHealth += amount;
    }

    public void decreaseMaxHealth(int amount) {
        this.maxHealth -= amount;
        if(getCurrentHealth() > getMaxHealth()) {
            currentHealth = maxHealth;
        }
    }

    public void increaseCurrentHealth(int amount) {
        if(currentHealth + amount > maxHealth) {
            currentHealth = maxHealth;
        } else {
            currentHealth += amount;
        }
    }

    public void decreaseCurrentHealth(int amount) {
        if(currentHealth - amount < 0) {
            currentHealth = 0;
        } else {
            currentHealth -= amount;
        }
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void refill() {
        currentHealth = maxHealth;
    }
}
