package View;

import Model.Level.Terrain;
import javafx.scene.image.Image;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Sprites {
    private static Sprites ourInstance;
    private String workingDir;

    private Map<Terrain, Image> terrainSprites;
    private Image fogSprite;
    private Image blackHex;
    private Image smasherSprite;
    private Image summonerSprite;
    private Image sneakSprite;

    private Sprites() { }

    public static Sprites getInstance() {
        if(ourInstance == null) {
            ourInstance = new Sprites();
        }

        return ourInstance;
    }

    public void initSprites() {
        workingDir = System.getProperty("user.dir");
        terrainSprites = new HashMap<>();
        terrainSprites.put(Terrain.GRASS, getImage(workingDir + "/src/View/Assets/hexTilesGrass1.png"));
        terrainSprites.put(Terrain.WATER, getImage(workingDir + "/src/View/Assets/water.png"));
        terrainSprites.put(Terrain.MOUNTAINS, getImage(workingDir + "/src/View/Assets/hexTilesMountain.png"));

        fogSprite = getImage(workingDir + "/src/View/Assets/fog.png");
        blackHex = getImage(workingDir + "/src/View/Assets/blackHex.png");

        smasherSprite = getImage(workingDir + "/src/View/Assets/warrior.png");
        summonerSprite = getImage(workingDir + "/src/View/Assets/mage.png");
        sneakSprite = getImage(workingDir + "/src/View/Assets/rogue.png");
    }

    private Image getImage(String fp) {
        File file = new File(fp);
        Image image = new Image(file.toURI().toString());
        return image;
    }

    public Image getTerrainSprite(Terrain terrain) {
        return terrainSprites.get(terrain);
    }
    public Image getFogSprite() { return fogSprite; }
    public Image getBlackHex() { return blackHex; }

    public Image getSmasherSprite() {
        return smasherSprite;
    }

    public Image getSneakSprite() {
        return sneakSprite;
    }

    public Image getSummonerSprite() {
        return summonerSprite;
    }
}
