package View.LevelView;

import Model.Level.River;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class RiverView extends LevelViewElement{
    private River river;

    public RiverView(River river, Point3D location) {
        super(location);

        this.river = river;

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/pointer.png");

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
