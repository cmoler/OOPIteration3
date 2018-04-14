package Model.Entity.EntityAttributes;

public class Speed {

    private int speed;

    public Speed() {
        speed = 1;
    }

    public Speed(int speed) {
        this.speed = speed;
    }

    public void increaseSpeed(int amount) {
        speed += amount;
    }

    public void decreaseSpeed(int amount) {
        if(speed - amount < 0) {
            speed = 0;
        }

        else {
            speed -= amount;
        }
    }
}
