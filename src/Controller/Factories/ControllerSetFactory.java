package Controller.Factories;

import Controller.Controller;
import Controller.ModelKeyAction.*;

import java.util.ArrayList;

public class ControllerSetFactory {

    Controller controller;
    KeyBindingParser keyBindingParser;
    public ControllerSetFactory(Controller controller){
        this.controller = controller;
        keyBindingParser = new KeyBindingParser();
    }

    public void createTradeSet() throws Exception {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new SelectKeyAction(keyBindingParser.parseMenuKey("select")));
        newKeySet.add(new ScrollLeftKeyAction(keyBindingParser.parseMenuKey("scrollLeft")));
        newKeySet.add(new ScrollRightKeyAction(keyBindingParser.parseMenuKey("scrollRight")));
        newKeySet.add(new ScrollUpKeyAction(keyBindingParser.parseMenuKey("scrollUp")));
        newKeySet.add(new ScrollDownKeyAction(keyBindingParser.parseMenuKey("scrollDown")));

        controller.setKeyActionSet(newKeySet);
    }

    public void createOptionsMenuSet(){

    }

    public void createPlayerControlsSet() throws Exception {
        ArrayList<ModelKeyAction> newKeySet = new ArrayList<>();

        newKeySet.add(new AttackKeyAction(keyBindingParser.parsePlayerKey("attack")));
        newKeySet.add(new RemoveTrapKeyAction(keyBindingParser.parsePlayerKey("removeTrap")));
        newKeySet.add(new BindWoundsKeyAction(keyBindingParser.parsePlayerKey("bindWounds")));
        newKeySet.add(new MoveNKeyAction(keyBindingParser.parsePlayerKey("moveN")));
        newKeySet.add(new MoveNEKeyAction(keyBindingParser.parsePlayerKey("moveNE")));
        newKeySet.add(new MoveSEKeyAction(keyBindingParser.parsePlayerKey("moveSE")));
        newKeySet.add(new MoveSKeyAction(keyBindingParser.parsePlayerKey("moveS")));
        newKeySet.add(new MoveSWKeyAction(keyBindingParser.parsePlayerKey("moveSW")));
        newKeySet.add(new MoveNWKeyAction(keyBindingParser.parsePlayerKey("moveNW")));
        newKeySet.add(new HotKey1KeyAction(keyBindingParser.parsePlayerKey("hotKey1")));
        newKeySet.add(new HotKey2KeyAction(keyBindingParser.parsePlayerKey("hotKey2")));
        newKeySet.add(new HotKey3KeyAction(keyBindingParser.parsePlayerKey("hotKey3")));
        newKeySet.add(new HotKey4KeyAction(keyBindingParser.parsePlayerKey("hotKey4")));
        newKeySet.add(new HotKey5KeyAction(keyBindingParser.parsePlayerKey("hotKey5")));

        controller.setKeyActionSet(newKeySet);
    }
}
