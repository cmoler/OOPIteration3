package View.MenuView;

import Configs.Commons;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.MenuModel.MenuModel;
import Model.MenuModel.StatsMenu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class StatsView extends InGameMenuView {

    Entity player;

    public StatsView(MenuModel menuModel) {
        super(menuModel);
    }

    @Override
    protected void renderSubMenu(GraphicsContext gc) {
        player = ((StatsMenu)menuModel.getActiveState()).getPlayer();

        renderStats(gc);
        renderPlayerSprite(gc);
        renderEquipment(gc);
    }

    private void renderStats(GraphicsContext gc) {
        int startX = Commons.SCREEN_WIDTH / 4 + 20;;
        int startY = Commons.SCREEN_HEIGHT / 8;

        int width = Commons.SCREEN_WIDTH / 16;
        int height = Commons.SCREEN_HEIGHT / 8;

        gc.setFont(new Font(40.0f).font("System", FontWeight.BOLD, 40.0f));
        gc.setFill(Color.BLACK);
        gc.fillText("Health: " + Integer.toString(player.getCurrentHealth()), startX, startY);
        gc.fillText("Mana: " + Integer.toString(player.getCurrentMana()), startX, startY+height);
        gc.fillText("Gold: " + Integer.toString(player.getCurrentGold()), startX, startY+2*height);

    }

    private void renderPlayerSprite(GraphicsContext gc) {

    }

    private void renderEquipment(GraphicsContext gc) {
        int startX = Commons.SCREEN_WIDTH / 4 + 20;
        int startY = Commons.SCREEN_HEIGHT / 2;
        int width = Commons.SCREEN_WIDTH / 6;
        int height = Commons.SCREEN_HEIGHT / 4;

        WeaponItem weaponItem = player.getEquipment().getEquippedWeapon();
        ArmorItem armorItem = player.getEquipment().getEquippedArmor();
        RingItem ringItem = player.getEquipment().getEquippedRing();

        gc.setFont(new Font(30.0f).font("System", FontWeight.BOLD, 30.0f));
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);

        if(weaponItem == null){
            gc.fillText("No Weapon \nEquipped", startX + width / 6, startY + 2 * height / 5);
        }else{

        }

        if(armorItem == null){
            gc.fillText("No Armor \nEquipped", startX + width + width / 6, startY + 2 * height / 5);
        }else{

        }

        if(ringItem == null){
            gc.fillText("No Ring \nEquipped", startX + 2 * width + width / 6, startY + 2 * height / 5);
        }else{

        }

        gc.strokeRect(startX, startY, width, height);
        gc.strokeRect(startX + width, startY, width, height);
        gc.strokeRect(startX + 2 * width, startY, width, height);

        if(selectedX != 0){
            gc.drawImage(selected, startX + (selectedX - 1) * width, startY, width, height);
        }
    }
}
