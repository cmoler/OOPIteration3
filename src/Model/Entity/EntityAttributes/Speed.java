package Model.Entity.EntityAttributes;

public class Speed {
    private int speed;

    public Speed() {
        speed = 1;
    }

    public Speed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void increaseSpeed(int amount) {
        speed += amount;
    }

    public void increaseSpeed(Speed add){
        speed += add.getSpeed();
    }

    public void decreaseSpeed(int amount) {
        if(speed - amount < 0) {
            speed = 0;
        }
        else {
            speed -= amount;
        }
    }
    
    public void decreaseSpeed(Speed dec) {
        int amount = dec.getSpeed();
        if(speed - amount < 0) {
            speed = 0;
        }
        else {
            speed -= amount;
        }
    }
}
