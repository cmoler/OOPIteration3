package View.LevelView;

import Model.InfluenceEffect.InfluenceEffect;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;

import java.io.File;

public class InfluenceEffectView extends LevelViewElement { // TODO: implement

  //  private InfluenceEffect effect; TODO: needed?

    public InfluenceEffectView(/*InfluenceEffect effect,*/ Point3D location) {
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