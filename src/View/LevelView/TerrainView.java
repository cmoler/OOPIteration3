package View.LevelView;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.io.File;

public class TerrainView implements LevelViewElement{
    private Point3D location;
    private Image hex;
    public TerrainView(Point3D location) {
        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/hex.png");
        hex = new Image(file.toURI().toString());

        this.location = location;
    }


    @Override
    public void notifyViewElement() {

    }

    public void render(GraphicsContext gc) {
        int size = 100;
        int width = size;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = -(int)(location.getY() + location.getZ());
        int yOffset = -(int) (2*location.getY() + location.getX());


        gc.drawImage(hex, 100+(int)((xOffset*width)*.75), 100 + (yOffset*(height/2)), width, height);
        //gc.drawImage(hex, 100 + (int)(width * .75), 100 + height/2 , width, height);

    }
}
