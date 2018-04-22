package View.LevelView;

import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class ItemView extends LevelViewElement{
    private boolean toRender;
    public ItemView(Point3D location) {
        super(location, 2);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/sword.png");

        setSprite(new Image(file.toURI().toString()));

        toRender = true;


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


    public void setOneHandedWeapon() {

    }

    public void setTwoHandedWeapon() {

    }

    public void setBrawlerWeapon() {

    }

    public void setStaff() {

    }

    public void setRangedWeapon() {

    }
}
