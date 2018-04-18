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
    private Orientation entityOrientation;
    private Image entitySprite;


    public EntityView(Entity entity) {
        super(new Point3D(0, 0, 0));
        entity.addObserver(this);
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/warrior.png");
        entitySprite = new Image(file.toURI().toString());
        setSprite(entitySprite);

        entityOrientation = entity.getOrientation();




    }
    @Override
    public void notifyViewElement() {
        entityOrientation = entity.getOrientation();
    }



    @Override
    public int getRenderPriority() {
        return 0;
    }


}
