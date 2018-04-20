package View;

import Model.Level.Terrain;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sprites {
    private static Sprites ourInstance = new Sprites();
    private String workingDir;

    private Map<Terrain, Image> terrainSprites;

    public static Sprites getInstance() {
        return ourInstance;
    }

    private Sprites() {
        workingDir = System.getProperty("user.dir");

        initSprites();
    }

    private void initSprites() {
        terrainSprites = new HashMap<>();
        terrainSprites.put(Terrain.GRASS, getImage(workingDir + "/src/View/Assets/hexTilesGrass1.png"));
        terrainSprites.put(Terrain.WATER, getImage(workingDir + "/src/View/Assets/water.png"));
        terrainSprites.put(Terrain.MOUNTAINS, getImage(workingDir + "/src/View/Assets/hexTilesMountain.png"));
    }

    private Image getImage(String fp) {
        File file = new File(fp);
        Image image = new Image(file.toURI().toString());
        return image;
    }

    public Image getTerrainSprite(Terrain terrain) {
        return terrainSprites.get(terrain);
    }
}
