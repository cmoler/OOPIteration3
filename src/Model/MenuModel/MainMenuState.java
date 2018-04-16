package Model.MenuModel;

public class MainMenuState extends MenuState {

    public MainMenuState(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    public void correctParameters() {
        if(selectedLeftRight != 0) selectedLeftRight = 0;
        if(selectedUpDown < 0) selectedUpDown = 4;
        if(selectedUpDown > 4) selectedUpDown = 0;
    }

    @Override
    public void select() {
        switch (selectedUpDown){
            case 0:
                menuModel.setActiveState(new NewGameMenu(menuModel));
                break;
            case 1:
                menuModel.setActiveState(new SaveGameMenu(menuModel));
                break;
            case 2:
                menuModel.setActiveState(new LoadGameMenu(menuModel));
                break;
            case 3:
                menuModel.setActiveState(new OptionsMenu(menuModel));
                break;
            case 4:
                System.exit(1);
                break;
        }
    }
}
