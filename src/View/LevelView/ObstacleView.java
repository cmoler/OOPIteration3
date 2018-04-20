package View.LevelView;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class ObstacleView extends LevelViewElement{



    public ObstacleView(Point3D location) {
        super(location, 2);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/boulder.png");

        setSprite(new Image(file.toURI().toString()));




    }


    @Override
    public void notifyViewElement() {

    }






}
