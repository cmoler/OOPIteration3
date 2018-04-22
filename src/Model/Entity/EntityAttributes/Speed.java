package Model.Entity.EntityAttributes;

public class Speed {
    private long speed;

    public Speed() {
        speed = 0500000000l;
    } // half a second move speed delay

    public Speed(long speed) {
        this.speed = speed;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public void increaseSpeed(long amount) {
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
        long amount = dec.getSpeed();
        if(speed - amount < 0) {
            speed = 0;
        }
        else {
            speed -= amount;
        }
    }
}
