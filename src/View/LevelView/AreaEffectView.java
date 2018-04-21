package View.LevelView;

import Model.AreaEffect.AreaEffect;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class AreaEffectView extends LevelViewElement { // TODO: implement



    public AreaEffectView(Point3D location) {
        super(location, 2);
    //    this.effect = effect; TODO: needed?


        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/star.png");

        setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {

    }

    @Override
    public void notifyViewElementDeath() {

    }


}
