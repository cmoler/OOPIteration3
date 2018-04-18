package View.LevelView;

import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class ItemView extends LevelViewElement{



    public ItemView(Point3D location) {
        super(location);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/sword.png");

        setSprite(new Image(file.toURI().toString()));




    }


    @Override
    public void notifyViewElement() {

    }



    @Override
    public int getRenderPriority() {
        return 3;
    }


}
