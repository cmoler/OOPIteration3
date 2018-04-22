package View.LevelView;

import Model.Level.Decal;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class DecalView extends LevelViewElement { // TODO: implement

    // private Decal decal; TODO: needed?

    public DecalView(Point3D location) {
        super(location, 2);
        //  this.decal = decal; TODO: needed?

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/skull.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {

    }

    @Override
    public void notifyViewElementDeath() {

    }


}