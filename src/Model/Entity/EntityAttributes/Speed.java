package Model.Entity.EntityAttributes;

public class Speed {
    private int speed;

    public Speed(int spd){
        speed = spd;
    }

    public void increaseSpeed(int increase){
        speed += increase;
    }

    public void increaseSpeed(Speed add){
        speed += add.getSpeed();
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

}
