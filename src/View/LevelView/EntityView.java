package View.LevelView;

import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

import java.io.File;

public class EntityView implements LevelViewElement{

    private Point3D entityLocation;
    private Orientation entityOrientation;
    private Image entitySprite;
    private int entitySize;
    private HexMathHelper hexMathHelper;

    public EntityView(Point3D startPos, int size, Orientation orientation) {

        String workingDir = System.getProperty("user.dir");

        File file = new File(workingDir + "/src/View/Assets/warrior.png");
        entitySprite = new Image(file.toURI().toString());

        entityLocation = startPos;
        entityOrientation = orientation;
        entitySize = size;

        hexMathHelper = new HexMathHelper();
    }
    @Override
    public void notifyViewElement() {

    }

    public void render(GraphicsContext gc) {
        int width = entitySize;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(entityLocation);
        int yOffset = hexMathHelper.getYCoord(entityLocation);

        rotate(gc, (double)entityOrientation.getDegreeOfOrientation(entityOrientation), 100+(int)((xOffset*width)*.75)+(width/2), 100 + (yOffset*(height/2))+(height/2));
        gc.drawImage(entitySprite, 100+(int)((xOffset*width)*.75), 100 + (yOffset*(height/2)), width, height);
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
}
