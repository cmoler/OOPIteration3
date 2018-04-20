package View.LevelView;

import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class EntityView extends LevelViewElement{

    private Entity entity;

    public EntityView(Entity entity, Point3D location) {
        super(location, 1);
        this.entity = entity;
        entity.addObserver(this);
        setOrientation(entity.getOrientation());

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/warrior.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {
        setOrientation(entity.getOrientation());
    }


    public void setAsPet() {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/pet_chicken.png");

        setSprite(new Image(file.toURI().toString()));
    }
}
