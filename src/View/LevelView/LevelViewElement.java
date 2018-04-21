package View.LevelView;

import Configs.Commons;
import Model.Entity.EntityAttributes.Orientation;
import View.Sprites;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public abstract class LevelViewElement {
    private Point3D location;
    private Point3D renderLocation;
    private Orientation orientation;
    private Orientation renderOrientation;
    private int size;
    private int renderPriority;
    private Image sprite;
    private Image fogSprite;
    private HexMathHelper hexMathHelper;

    protected LevelViewElement(Point3D location, int renderPriority) {
        this.location = location;
        renderLocation = location;
        orientation = Orientation.NORTH;
        renderOrientation = orientation;
        hexMathHelper = new HexMathHelper();
        size = 75;
        this.renderPriority = renderPriority;


        fogSprite = Sprites.getInstance().getFogSprite();
    }

    public abstract void notifyViewElement();

    public void render(GraphicsContext gc, Point2D playerPos, Point2D scrollOffset) {


        int width = size;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(renderLocation)-(int)playerPos.getX();
        int yOffset = hexMathHelper.getYCoord(renderLocation) - (int)playerPos.getY();

        rotate(gc, renderOrientation.getDegreeOfOrientation(renderOrientation), ((xOffset*width)*.75)+(width/2) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2))+(height/2) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY());
        gc.drawImage(sprite, (int)((xOffset*width)*.75) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2)) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY(), width, height);

        if(renderLocation != location) {
            gc.drawImage(fogSprite, (int)((xOffset*width)*.75) + Commons.SCREEN_WIDTH/2 + scrollOffset.getX(), (yOffset*(height/2)) + Commons.SCREEN_HEIGHT/2 + scrollOffset.getY(), width, height);

        }

    }

    protected void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    public int getRenderPriority() {
        return renderPriority;
    }

    public void setRenderPriority(int renderPriority) {
        this.renderPriority = renderPriority;
    }

    public void locationViewedByPlayer() {
        renderLocation = location;
        renderOrientation = orientation;
    }

    public void rendererLocationViewedByPlayer() {
        if(renderLocation != location) {
            renderLocation = new Point3D(-50, -50, 100);
        }
    }

    protected void setSprite(Image sprite) {
        this.sprite = sprite;
    }

    protected void setOrientation(Orientation newOrientation) {
        orientation = newOrientation;
    }

    public void setPosition(Point3D position) {
        location = position;
    }

    public Point3D getLocation() {
        return location;
    }
    public Point3D getRenderLocation() { return renderLocation; }


    public int getSize() {
        return size;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
