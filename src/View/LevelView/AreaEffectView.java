package View.LevelView;

import Model.AreaEffect.AreaEffect;
import javafx.geometry.Point3D;

public class AreaEffectView extends LevelViewElement { // TODO: implement

   // private AreaEffect effect; TODO: needed?

    public AreaEffectView(/*AreaEffect effect,*/ Point3D location) {
        super(location, 2);
    //    this.effect = effect; TODO: needed?


     //   String workingDir = System.getProperty("user.dir");

       // File file = new File(workingDir + "/src/View/Assets/warrior.png");

     //   setSprite(new Image(file.toURI().toString()));
    }

    @Override
    public void notifyViewElement() {

    }

    @Override
    public void notifyViewElementDeath() {

    }


}
