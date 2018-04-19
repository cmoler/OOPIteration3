package View.LevelView;


import Model.Entity.EntityAttributes.Orientation;
import Model.Level.Terrain;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TerrainView extends LevelViewElement{
    private Point3D location;
    private Image hex;
    private Map<Terrain, Image> sprites;

    public TerrainView(Terrain terrain, Point3D location) {
        super(location);
        String workingDir = System.getProperty("user.dir");
        sprites = new HashMap<>();

        File file = new File(workingDir + "/src/View/Assets/hexTilesGrass1.png");

        sprites.put(Terrain.GRASS, new Image(file.toURI().toString()));

        file = new File(workingDir + "/src/View/Assets/water.png");
        sprites.put(Terrain.WATER, new Image(file.toURI().toString()));

        file = new File(workingDir + "/src/View/Assets/hexTilesMountain.png");
        sprites.put(Terrain.MOUNTAINS, new Image(file.toURI().toString()));


        setSprite(sprites.get(terrain));
        this.location = location;



    }


    @Override
    public void notifyViewElement() {

    }



    @Override
    public int getRenderPriority() {
        return 4;
    }


}
