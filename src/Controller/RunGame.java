package Controller;

import View.LevelView.EntityView;
import View.LevelView.TerrainView;
import javafx.animation.AnimationTimer;
import javafx.application.Application;

import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import Model.Entity.EntityAttributes.Orientation;


public class RunGame extends Application{
    private Stage mainStage;
    private Scene mainScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){

        //Initialization Shit
        mainStage = primaryStage;
        mainStage.setTitle("HYPED MEAN PENGUINS BACK FOR DAVE SMALL DAVE MEDIUM DAVE LARGE DAVE MORE");

        Group root = new Group();
        mainScene = new Scene(root);
        primaryStage.setScene( mainScene );

        Canvas canvas = new Canvas(900, 900);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);


        TerrainView tv1 = new TerrainView(new Point3D(0, 0, 0), 100);
        TerrainView tv2 = new TerrainView(new Point3D(1, 0, -1), 100);
        TerrainView tv3 = new TerrainView(new Point3D(1, -1, 0), 100);
        TerrainView tv4 = new TerrainView(new Point3D(2, -1, -1), 100);
        EntityView ev = new EntityView(new Point3D(0, 0, 0), 100, Orientation.SOUTHEAST);
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                tv1.render(gc);
                tv2.render(gc);
                tv3.render(gc);
                tv4.render(gc);
                ev.render(gc);
            }
        }.start();



        mainStage.show();

    }
}
