package Controller;

import Controller.Factories.ControllerSetFactory;
import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.PetAIFactory;
import Controller.Factories.SkillsFactory;
import Controller.Visitor.SavingVisitor;
import Model.AI.AIState;
import Model.AI.PetAI.PetPriority;
import Model.Entity.Entity;
import Model.Level.GameLoopMessenger;
import Model.Level.GameModel;
import Model.MenuModel.*;
import Model.Level.Level;
import Model.MenuModel.MainMenuState;
import Model.MenuModel.MenuModel;
import Model.MenuModel.MenuState;
import View.LevelView.HUDStatsView;
import View.LevelView.HotbarView;

import View.MenuView.*;

import View.LevelView.ObservationView;
import View.MenuView.MenuView;
import View.MenuView.MenuViewState;
import View.MenuView.TitleScreenView;

import View.Renderer;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.canvas.GraphicsContext;
import org.xml.sax.SAXException;

import javax.swing.plaf.synth.SynthOptionPaneUI;
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
    private boolean playerFresh = true;

    public GameLoop() {
        gameLoopMessenger = new GameLoopMessenger(this);
        gameLoader = new GameLoader(this);
    }

    public void init() {
        scrollOffSet = new Point2D(0, 0);
        controls = new KeyEventImplementor(this);
        menuModel = new MenuModel(this);
//        gameModel = new GameModel(gameLoopMessenger);
//
//        gameModel.initWorldMap();
        renderer = new Renderer();
        ((KeyEventImplementor) controls).createMainMenuSet(menuModel);
        setMenuState(new MainMenuState(menuModel, this), new TitleScreenView(menuModel));
    }

    public void openBarterWindow(Entity playerEntity, Entity receivingEntity) {
        if(playerEntity == null || receivingEntity == null) {
            // do nothing if either entity is null
        }else{
            SkillsFactory skillsFactory = gameModel.getSkillsFactory();
            int barterLevel = playerEntity.getSkillLevel(skillsFactory.getBargainSkill());
            setMenuState(new BarterMenu(menuModel, this, barterLevel, playerEntity, receivingEntity), new BarterView(menuModel));
            setInGameMenuKeySet();
        }
    }

    public void openDialogWindow(Entity playerEntity, Entity receivingEntity) {
        boolean wantToTalk = gameModel.getAIForEntity(receivingEntity).wantToTalk();
        setMenuState(new DialogMenu(menuModel, this, wantToTalk, playerEntity, receivingEntity), new DialogView(menuModel));
        setInGameMenuKeySet();
    }

    public void createObservationWindow(Entity entity, String randomEntityFacts) {
        renderer.addObservationView(new ObservationView(entity, randomEntityFacts));
    }

    public void loadGame(int i) {
        playerFresh = true;
        try {
            switch (i) {
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
            try {
                gameLoader.loadGame("SMASHER.xml");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SAXException e1) {
                e1.printStackTrace();
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            }
        } finally {
            renderer.closeMenu();
            gameModel = gameLoader.getGameModel();
            ((KeyEventImplementor) controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);

            renderer.setPlayerHUD(new HUDStatsView(gameModel.getPlayer()));
            renderer.setHotBarView(new HotbarView(gameModel.getPlayer()));

            for (Level level : gameModel.getLevels()) {
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
        playerFresh = true;
        try {
            switch (i) {
                case 0:
                    gameLoader.loadGame("SMASHER.xml");
                    break;

                case 1:
                    gameLoader.loadGame("SUMMONER.xml");
                    break;

                case 2:
                    gameLoader.loadGame("SNEAK.xml");
                    break;
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("FILE NOT FOUND");
            try {
                gameLoader.loadGame("SMASHER.xml");
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (SAXException e1) {
                e1.printStackTrace();
            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
            }
        } finally {
            renderer.closeMenu();
            gameModel = gameLoader.getGameModel();

            ((KeyEventImplementor) controls).createPlayerControlsSet(gameModel.getPlayer(), menuModel);

            renderer.setPlayerHUD(new HUDStatsView(gameModel.getPlayer()));
            renderer.setHotBarView(new HotbarView(gameModel.getPlayer()));

            for (Level level : gameModel.getLevels()) {
                renderer.loadModelSprites(level);
            }

            renderer.updateCurrentLevel(gameLoader.getCurrentLevel());

            runGame.startGame();
        }
    }

    public void setMenuState(MenuState menuState, MenuViewState menuViewState){
        if(runGame.hasStarted()) {
            runGame.pauseGame();
        }

        menuModel.setActiveState(menuState);
        renderer.setActiveMenuState(menuViewState);
    }

    private void setGameOver() {
        setMenuState(new GameOverMenu(menuModel, this), new GameOverView(menuModel));
        setInGameMenuKeySet();
    }

    public void tick() {
        gameModel.advance();
        renderer.updateCurrentLevel(gameModel.getCurrentLevel());
        if(gameModel.playerIsDead() && playerFresh) {
            //gameModel.resetPlayer();
            setGameOver();
            playerFresh = false;
        }
        else{
            gameModel.advance();
        }
    }

    public void render(GraphicsContext gc){
        if(gameModel != null) {
            renderer.updateTerrainFog(gameModel.getCurrentLevel(), gameModel.getPlayerPosition(), gameModel.getPlayer().getSight());
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
        runGame.startGame();
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

    public PetAIFactory getPetAIFactoryFromCurrentLevel() {
        return gameModel.getPetAIFactory();
    }

    public void setAIOnCurrentLevel(Entity entity, AIState aiState) {
        gameModel.setAIOnCurrentLevel(entity, aiState);
    }

    public void setAIPriorityOnCurrentLevel(Entity entity, PetPriority petPriority) {
        gameModel.setAIPriorityOnCurrentLevel(entity, petPriority);
    }
}
