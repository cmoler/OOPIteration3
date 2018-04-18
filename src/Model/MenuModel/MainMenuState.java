package Model.MenuModel;

import Controller.GameLoop;
import View.MenuView.AssignItemView;
import View.MenuView.LoadGameView;
import View.MenuView.NewGameView;
import View.MenuView.OptionsView;

public class MainMenuState extends MenuState {

    public MainMenuState(MenuModel menuModel, GameLoop gameLoop) {
        super(menuModel, gameLoop);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight != 0) selectedLeftRight = 0;
        if(selectedUpDown < 0) selectedUpDown = 3;
        if(selectedUpDown > 3) selectedUpDown = 0;
    }

    @Override
    public void select() {
        switch (selectedUpDown){
            case 0:
                gameLoop.setMenuState(new NewGameMenu(menuModel, gameLoop), new NewGameView(menuModel));
                break;
            case 1:
                System.out.println("opening load game");
                gameLoop.setMenuState(new LoadGameMenu(menuModel, gameLoop), new LoadGameView(menuModel));
                break;
            case 2:
                gameLoop.setMenuState(new OptionsMenu(menuModel, gameLoop), new OptionsView(menuModel));
                break;
            case 3:
                System.exit(1);
                break;
        }
    }
}
