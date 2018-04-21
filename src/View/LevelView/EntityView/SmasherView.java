package View.LevelView.EntityView;

import Model.Entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class SmasherView extends EntityView {

    public SmasherView(Entity entity, Point3D location) {
        super(entity, location);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/warrior.png");

        setSprite(new Image(file.toURI().toString()));
    }
}
