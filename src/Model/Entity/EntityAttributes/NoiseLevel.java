package Model.Entity.EntityAttributes;

public class NoiseLevel {

    private int noise;

    public NoiseLevel() {
        noise = 1;
    }

    public NoiseLevel(int noise) {
        this.noise = noise;
    }

    public void increaseNoise(int amount) {
        noise += amount;
    }

    public void decreaseNoise(int amount) {
        if(noise - amount < 0) {
            noise = 0;
        }

        else {
            noise -= amount;
        }
    }
}
