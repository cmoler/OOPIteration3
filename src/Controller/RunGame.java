package Controller;

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
        mainStage.setTitle("HYPEDMEANPENGUINSBACKFORDAVESMALLDAVEMEDIUMDAVELARGEDAVEMORE");

        Group root = new Group();
        mainScene = new Scene(root);
        primaryStage.setScene( mainScene );

        Canvas canvas = new Canvas(900, 900);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setFocusTraversable(true);


        TerrainView tv1 = new TerrainView(new Point3D(0, 0, 0));
        TerrainView tv2 = new TerrainView(new Point3D(1, 0, -1));
        TerrainView tv3 = new TerrainView(new Point3D(1, -1, 0));
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                tv1.render(gc);
                tv2.render(gc);
                tv3.render(gc);
            }
        }.start();



        mainStage.show();

    }
}
