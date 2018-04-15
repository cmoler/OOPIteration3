package View.LevelView;

import javafx.geometry.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class EntityView implements LevelViewElement{

    private Point3D entityLocation;
    private Orientation entityOrientation;
    private Image entitySprite;
    private int entitySize;

    public EntityView(Point3D startPos, int size) {

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/hex.png");
        entitySprite = new Image(file.toURI().toString());

        entityLocation = startPos;
        entitySize = size;
    }
    @Override
    public void notifyViewElement() {

    }

    public void render(GraphicsContext gc) {

    }
}
