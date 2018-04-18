package Model.MenuModel;

import Controller.GameLoop;

public class OptionsMenu extends MenuState {

    private KeyBindings keyBindings;

    public OptionsMenu(MenuModel menuModel, GameLoop gameLoop) {
        super(menuModel, gameLoop);
        keyBindings = new KeyBindings();
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight < 0) selectedLeftRight = keyBindings.getNumberOfBindings() - 1;
        if(selectedLeftRight > keyBindings.getNumberOfBindings() - 1) selectedLeftRight = 0;
        if(selectedUpDown < 0) selectedUpDown = keyBindings.getNumberOfKeysForBinding(selectedLeftRight) - 1;
        if(selectedUpDown > keyBindings.getNumberOfKeysForBinding(selectedLeftRight) - 1) selectedUpDown = 0;
    }

    @Override
    public void select() {

    }
}
