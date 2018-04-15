package Controller.Factories;

import Controller.Controller;
import Controller.ModelKeyAction.*;
import Model.Entity.Entity;
import Model.MenuModel.MenuModel;

import java.util.ArrayList;

public class ControllerSetFactory {

    Controller controller;
    KeyBindingParser keyBindingParser;
    public ControllerSetFactory(Controller controller){
        this.controller = controller;
        keyBindingParser = new KeyBindingParser();
    }

    public void createTradeSet(MenuModel menuModel) {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new SelectKeyAction(keyBindingParser.parseMenuKey("select"), menuModel));
        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parseMenuKey("scrollLeft"), menuModel));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parseMenuKey("scrollRight"), menuModel));
        newKeySet.add(new ScrollUpKeyAction(keyBindingParser.parseMenuKey("scrollUp"), menuModel));
        newKeySet.add(new ScrollDownKeyAction(keyBindingParser.parseMenuKey("scrollDown"), menuModel));

        controller.setKeyActionSet(newKeySet);
    }

    public void createOptionsMenuSet(){

    }

    public void createScrollingViewPortSet(){

    }

    public void createPlayerControlsSet(Entity player) {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new AttackKeyAction(keyBindingParser.parsePlayerKey("attack"), player));

        newKeySet.add(new ToggleLockViewPortKeyAction(keyBindingParser.parsePlayerKey("toggleLockView"), player,this, false));

        newKeySet.add(new MoveNKeyAction(keyBindingParser.parsePlayerKey("moveN"), player));
        newKeySet.add(new MoveNEKeyAction(keyBindingParser.parsePlayerKey("moveNE"), player));
        newKeySet.add(new MoveSEKeyAction(keyBindingParser.parsePlayerKey("moveSE"), player));
        newKeySet.add(new MoveSKeyAction(keyBindingParser.parsePlayerKey("moveS"), player));
        newKeySet.add(new MoveSWKeyAction(keyBindingParser.parsePlayerKey("moveSW"), player));
        newKeySet.add(new MoveNWKeyAction(keyBindingParser.parsePlayerKey("moveNW"), player));

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
