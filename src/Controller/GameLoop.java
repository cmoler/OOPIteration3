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
        scrollOffSet = new Point2D(0, 0);
        controls = new KeyEventImplementor(this);
        menuModel = new MenuModel(this);
        renderer = new Renderer();
        ((KeyEventImplementor) controls).createMainMenuSet(menuModel);
        setMenuState(new MainMenuState(menuModel, this), new TitleScreenView(menuModel));
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
        try {
            switch(i) {
                case 0:
                    gameLoader.loadGame("SAVESLOT1.xml");
                    break;

                case 1:
                    gameLoader.loadGame("SAVESLOT2.xml");
                    break;

                case 2:
                    gameLoader.loadGame("SAVESLOT3.xml");
                    break;

                case 3:
                    gameLoader.loadGame("SAVESLOT4.xml");
                    break;

            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("FILE NOT FOUND");
        }

        finally {
            renderer.closeMenu();
            gameModel = gameLoader.getGameModel();
            ((KeyEventImplementor) controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);

            renderer.setPlayerHUD(new HUDStatsView(gameModel.getPlayer()));
            renderer.setHotBarView(new HotbarView(gameModel.getPlayer()));

            for(Level level: gameModel.getLevels()) {
                renderer.loadModelSprites(level);
            }

            renderer.updateCurrentLevel(gameLoader.getCurrentLevel());

            runGame.startGame();
        }
    }

    public void saveGame(int i) {
        try {
            switch (i) {
                case 0:
                    gameSaver = new SavingVisitor("SAVESLOT1.xml");
                    break;

                case 1:
                    gameSaver = new SavingVisitor("SAVESLOT2.xml");
                    break;

                case 2:
                    gameSaver = new SavingVisitor("SAVESLOT3.xml");
                    break;

                case 3:
                    gameSaver = new SavingVisitor("SAVESLOT4.xml");
                    break;
            }

            gameSaver.visitGameModel(gameModel);
        }

        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void newGame(int i) {

    }

    public void setMenuState(MenuState menuState, MenuViewState menuViewState){
        if(runGame.hasStarted()) {
            runGame.pauseGame();
        }

        menuModel.setActiveState(menuState);
        renderer.setActiveMenuState(menuViewState);
    }

    public void tick() {
        gameModel.advance();
    }

    public void render(GraphicsContext gc){
        if(gameModel != null) {
            renderer.updateTerrainFog(gameModel.getPlayerPosition(), gameModel.getPlayer().getSight());
            renderer.render(gc, gameModel.getPlayerPosition(), scrollOffSet);
        }

        else {
            renderer.renderMenu(gc);
        }
    }

    public KeyEventImplementor getControls() {
        return ((KeyEventImplementor)controls);
    }

    public void setControls(){
        controls = new KeyEventImplementor(this);
        ((KeyEventImplementor)controls).createMainMenuSet(menuModel);
        runGame.setInput(controls);
        runGame.startGame();
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
