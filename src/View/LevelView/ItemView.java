package View.LevelView;

import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.util.Random;

public class ItemView extends LevelViewElement{
    private boolean toRender;
    private Random rand;
    public ItemView(Point3D location) {
        super(location, 2);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/sword.png");

        setSprite(new Image(file.toURI().toString()));

        toRender = true;
        rand = new Random();



    }

    @Override
    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {
        if(toRender) {
            super.render(gc, playerPos, scrollOffset);
        }
    }

    @Override
    public void notifyViewElement() {

    }

    @Override
    public void notifyViewElementDeath() {

    }

    public void setToRender(boolean toRender) {
        this.toRender = toRender;
    }

    public void setOneHandedSword() {
        String workingDir = System.getProperty("user.dir");
        int randInt = (rand.nextInt(3))+1;
        System.out.println(randInt);
        File file = new File(workingDir + "/src/View/Assets/OneHandedSword" + Integer.toString(randInt) + ".png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setStaff() {
        String workingDir = System.getProperty("user.dir");

        int randInt = (rand.nextInt(3))+1;
        File file = new File(workingDir + "/src/View/Assets/OneHandedStaff" + Integer.toString(randInt) + ".png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setBow() {
        String workingDir = System.getProperty("user.dir");

        int randInt = (rand.nextInt(3))+1;
        File file = new File(workingDir + "/src/View/Assets/bow" + Integer.toString(randInt) + ".png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setBareFists() {
        String workingDir = System.getProperty("user.dir");

        int randInt = (rand.nextInt(2))+1;
        File file = new File(workingDir + "/src/View/Assets/glove" + Integer.toString(randInt) + ".png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setPotion() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/potion.png");

        setSprite(new Image(file.toURI().toString()));
    }

    public void setManaPotion() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/manaPotion.png");

        setSprite(new Image(file.toURI().toString()));
    }

}
