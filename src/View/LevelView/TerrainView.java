package View.LevelView;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class TerrainView implements LevelViewElement{
    private Point3D location;
    private Image hex;
    private HexMathHelper hexMathHelper;
    private int size;
    public TerrainView(Point3D location, int size) {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/hex.png");
        hex = new Image(file.toURI().toString());

        this.location = location;

        hexMathHelper = new HexMathHelper();

        this.size = size;
    }


    @Override
    public void notifyViewElement() {

    }

    public void render(GraphicsContext gc) {
        int width = size;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(location);
        int yOffset = hexMathHelper.getYCoord(location);


        gc.drawImage(hex, 100+(int)((xOffset*width)*.75), 100 + (yOffset*(height/2)), width, height);


    }
}
