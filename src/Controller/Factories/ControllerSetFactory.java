package Controller.Factories;

import Controller.Controller;
import Controller.GameLoop;
import Controller.ModelKeyAction.*;
import Model.Entity.Entity;
import Model.MenuModel.MenuModel;
import Model.MenuModel.TradingMenu;

import java.util.ArrayList;

public class ControllerSetFactory {

    private Controller controller;
    private KeyBindingParser keyBindingParser;
    private GameLoop gameLoop;

    public ControllerSetFactory(Controller controller, GameLoop gameLoop){
        this.controller = controller;
        this.gameLoop = gameLoop;
        keyBindingParser = new KeyBindingParser();
    }

    public void createTradeSet(MenuModel menuModel, Entity player, Entity npc) {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        menuModel.setActiveState(new TradingMenu(menuModel, gameLoop, player, npc));

        newKeySet.add(new SelectKeyAction(keyBindingParser.parseMenuKey("select"), menuModel));
        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parseMenuKey("scrollLeft"), menuModel));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parseMenuKey("scrollRight"), menuModel));
        newKeySet.add(new ScrollUpKeyAction(keyBindingParser.parseMenuKey("scrollUp"), menuModel));
        newKeySet.add(new ScrollDownKeyAction(keyBindingParser.parseMenuKey("scrollDown"), menuModel));

        controller.setKeyActionSet(newKeySet);
    }

    public void createInGameMenuSet(MenuModel menuModel){
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new CloseMenuKeyAction(keyBindingParser.parseMenuKey("closeMenu"), gameLoop));

        newKeySet.add(new SelectKeyAction(keyBindingParser.parseMenuKey("select"), menuModel));
        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parseMenuKey("scrollLeft"), menuModel));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parseMenuKey("scrollRight"), menuModel));
        newKeySet.add(new ScrollUpKeyAction(keyBindingParser.parseMenuKey("scrollUp"), menuModel));
        newKeySet.add(new ScrollDownKeyAction(keyBindingParser.parseMenuKey("scrollDown"), menuModel));

        controller.setKeyActionSet(newKeySet);
    }

    public void createMenuSet(MenuModel menuModel){
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new BackKeyAction(keyBindingParser.parseMenuKey("back"), menuModel, gameLoop));

        newKeySet.add(new SelectKeyAction(keyBindingParser.parseMenuKey("select"), menuModel));
        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parseMenuKey("scrollLeft"), menuModel));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parseMenuKey("scrollRight"), menuModel));
        newKeySet.add(new ScrollUpKeyAction(keyBindingParser.parseMenuKey("scrollUp"), menuModel));
        newKeySet.add(new ScrollDownKeyAction(keyBindingParser.parseMenuKey("scrollDown"), menuModel));
        
        controller.setKeyActionSet(newKeySet);
    }

    public void createPlayerControlsSet(Entity player, MenuModel menuModel) {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new OpenMenuKeyAction(keyBindingParser.parsePlayerKey("openMenu"), player, menuModel, gameLoop));

        newKeySet.add(new AttackKeyAction(keyBindingParser.parsePlayerKey("attack"), player));
        newKeySet.add(new DismountKeyAction(keyBindingParser.parsePlayerKey("dismount"), player));

        newKeySet.add(new MoveNKeyAction(keyBindingParser.parsePlayerKey("moveN"), player, gameLoop));
        newKeySet.add(new MoveNEKeyAction(keyBindingParser.parsePlayerKey("moveNE"), player, gameLoop));
        newKeySet.add(new MoveSEKeyAction(keyBindingParser.parsePlayerKey("moveSE"), player, gameLoop));
        newKeySet.add(new MoveSKeyAction(keyBindingParser.parsePlayerKey("moveS"), player, gameLoop));
        newKeySet.add(new MoveSWKeyAction(keyBindingParser.parsePlayerKey("moveSW"), player, gameLoop));
        newKeySet.add(new MoveNWKeyAction(keyBindingParser.parsePlayerKey("moveNW"), player, gameLoop));

        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parsePlayerKey("scrollLeft"), player));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parsePlayerKey("scrollRight"), player));
        newKeySet.add(new SelectKeyAction(keyBindingParser.parsePlayerKey("select"), player));


        newKeySet.add(new HotKey1KeyAction(keyBindingParser.parsePlayerKey("hotKey1"), player));
        newKeySet.add(new HotKey2KeyAction(keyBindingParser.parsePlayerKey("hotKey2"), player));
        newKeySet.add(new HotKey3KeyAction(keyBindingParser.parsePlayerKey("hotKey3"), player));
        newKeySet.add(new HotKey4KeyAction(keyBindingParser.parsePlayerKey("hotKey4"), player));
        newKeySet.add(new HotKey5KeyAction(keyBindingParser.parsePlayerKey("hotKey5"), player));

        controller.setKeyActionSet(newKeySet);
    }
}
