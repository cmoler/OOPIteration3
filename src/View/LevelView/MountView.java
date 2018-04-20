package View.LevelView;

import Model.Level.Mount;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class MountView extends LevelViewElement {

    private Mount mount;

    public MountView(Mount mount, Point3D location) {
        super(location);
        this.mount = mount;
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/horse.png");

        setSprite(new Image(file.toURI().toString()));

        mount.addObserver(this);
    }
    @Override
    public void notifyViewElement() {
        setOrientation(mount.getOrientation());
    }

    @Override
    public int getRenderPriority() {
        return 1;
    }
}
