package View.LevelView;

import Model.Level.Mount;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class MountView extends LevelViewElement{
    private Mount mount;
    public MountView(Mount mount, Point3D location) {
        super(location);

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/horse.png");

        setSprite(new Image(file.toURI().toString()));
    }
    @Override
    public void notifyViewElement() {

    }

    @Override
    public int getRenderPriority() {
        return 1;
    }
}
