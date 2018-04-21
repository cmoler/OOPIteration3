package View.LevelView.EntityView;

import Model.Entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class SneakView extends EntityView {

    public SneakView(Entity entity, Point3D location) {
        super(entity, location);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/rogue.png");

        setSprite(new Image(file.toURI().toString()));
    }
}
