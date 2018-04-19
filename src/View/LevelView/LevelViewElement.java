package View.LevelView;

import Model.Entity.EntityAttributes.Orientation;
import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.transform.Rotate;

public abstract class LevelViewElement {
    private Point3D location;
    private Orientation orientation;
    private int size;
    private Image sprite;
    private HexMathHelper hexMathHelper;
    protected boolean isWaitingToRender;

    LevelViewElement(Point3D location) {
        this.location = location;
        orientation = Orientation.NORTH;
        hexMathHelper = new HexMathHelper();
        size = 75;
        isWaitingToRender = true;
    }
    public abstract void notifyViewElement();
    public void render(GraphicsContext gc, Point2D offset) {
        //if(!isWaitingToRender) { return; }
        int width = size;
        int height = (int)(width * (Math.sqrt(3)/2));

        int xOffset = hexMathHelper.getXCoord(location);
        int yOffset = hexMathHelper.getYCoord(location);

        rotate(gc, orientation.getDegreeOfOrientation(orientation), ((xOffset*width)*.75)+offset.getX()+(width/2), (yOffset*(height/2))+offset.getY()+(height/2));
        gc.drawImage(sprite, (int)((xOffset*width)*.75)+offset.getX(), (yOffset*(height/2))+offset.getY(), width, height);



        isWaitingToRender = false;


    }
    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }
    public abstract int getRenderPriority();

    protected void setSprite(Image sprite) {
        this.sprite = sprite;
    }
    protected void setOrientation(Orientation newOrientation) {
        orientation = newOrientation;
        isWaitingToRender = true;
    }
    public void setPosition(Point3D position) {
        location = position;
        isWaitingToRender = true;
    }
}
