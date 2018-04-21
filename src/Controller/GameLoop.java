package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Visitor.SavingVisitor;
import Model.Entity.Entity;
import Model.Level.GameLoopMessenger;
import Model.Level.GameModel;
import Model.Level.Level;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import Model.MenuModel.MenuState;
import View.LevelView.HUDStatsView;
import View.LevelView.HotbarView;
import View.LevelView.ObservationView;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import View.MenuView.TitleScreenView;
import View.Renderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.GraphicsContext;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;


public class GameLoop {

    private AnimationTimer loopTimer;
    private GameLoopMessenger gameLoopMessenger;
    private GameModel gameModel;
    private MenuView menuView;
    private MenuModel menuModel;
    private SavingVisitor gameSaver;
    private EntityFactory entityFactory;
    private ControllerSetFactory controllerSetFactory;
    private Renderer renderer;
    private EventHandler<KeyEvent> controls;
    private RunGame runGame;
    private Point2D scrollOffSet;
    private GameLoader gameLoader;

    public GameLoop() {
        gameLoopMessenger = new GameLoopMessenger(this);
        gameLoader = new GameLoader(this);
    }

    public void init() {
        try {
            gameLoader.loadGame("SMASHER.xml");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        scrollOffSet = new Point2D(0, 0);
        controls = new KeyEventImplementor(this);
        menuModel = new MenuModel(this);

        gameModel = gameLoader.getGameModel();

        renderer = new Renderer();

        ((KeyEventImplementor) controls).createMainMenuSet(menuModel);
        setMenuState(new MainMenuState(menuModel, this), new TitleScreenView(menuModel));
        renderer.setPlayerHUD(new HUDStatsView(gameModel.getPlayer()));
        renderer.setHotBarView(new HotbarView(gameModel.getPlayer()));

        renderer.updateCurrentLevel(gameModel.getCurrentLevel());

        for (Level level : gameModel.getLevels()) {
            renderer.loadModelSprites(level);
        }

        renderer.updateCurrentLevel(gameModel.getCurrentLevel());
    }

    public void openBarterWindow(Entity playerEntity, int playerBarterStrength, Entity receivingEntity) {
        // TODO: implement
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }
    }

    public void openDialogWindow(Entity playerEntity, Entity receivingEntity) {
        // TODO: implement
        System.out.println("I (player) am talking to you!");
    }

    public void createObservationWindow(Point3D entityLocation, String randomEntityFacts) {
        // TODO: implement
        renderer.addObservationView(new ObservationView(entityLocation, randomEntityFacts));
    }

    public void loadGame(int i) {
        renderer.closeMenu();
        ((KeyEventImplementor) controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);
        gameModel = gameLoader.getGameModel();
        runGame.startGame();
    }

    public void saveGame(int i) {

    }

    public void newGame(int i) {

    }

    public void setMenuState(MenuState menuState, MenuViewState menuViewState){
        menuModel.setActiveState(menuState);
        renderer.setActiveMenuState(menuViewState);
    }

    public void tick() {
        gameModel.advance();
    }

    public void render(GraphicsContext gc){
        renderer.updateTerrainFog(gameModel.getPlayerPosition(), gameModel.getPlayer().getSight());
        renderer.render(gc, gameModel.getPlayerPosition(), scrollOffSet);
    }

    public KeyEventImplementor getControls() {
        return ((KeyEventImplementor)controls);
    }

    public void setControls(){
        controls = new KeyEventImplementor(this);
        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
        runGame.setInput(controls);
    }

    public void setKeyBinding(int selectedLeftRight, int selectedUpDown) {
        controls = new KeyBindingSetter(this, selectedLeftRight, selectedUpDown);
        runGame.setInput(controls);
    }

    public void setRunGame(RunGame runGame) {
        this.runGame = runGame;
    }

    public void closeMenu() {
        renderer.closeMenu();
        ((KeyEventImplementor)controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);
    }

    public void setInGameMenuKeySet() {
        ((KeyEventImplementor)controls).createInGameMenuSet(menuModel);
    }

    public void setMainMenuKeySet() {
        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
    }

    public void resetScrollOffSet() {
        this.scrollOffSet = Point2D.ZERO;
    }

    public void addScrollOffSet(Point2D mouseOffSet) {
        this.scrollOffSet = new Point2D(scrollOffSet.getX() + mouseOffSet.getX(), scrollOffSet.getY() + mouseOffSet.getY());
    }
}
