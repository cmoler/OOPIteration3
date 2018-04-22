package View.LevelView.EntityView;

import Model.Entity.Entity;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class PetView extends EntityView {

    public PetView(Entity entity, Point3D location) {
        super(entity, location);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/pet_chicken.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElementDeath() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/kfc.png");

        setSprite(new Image(file.toURI().toString()));
    }
}
