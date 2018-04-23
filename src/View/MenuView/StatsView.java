package View.MenuView;

import Configs.Commons;
import Model.Entity.Entity;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.MenuModel.MenuModel;
import Model.MenuModel.StatsMenu;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

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
        gc.fillText("Health: " + Integer.toString(player.getCurrentHealth()) + "/" + Integer.toString(player.getMaxHealth()), startX, startY);
        gc.fillText("Defense: " + Integer.toString(player.getDefensePoints()), startX, startY+height);
        gc.fillText("Mana: " + Integer.toString(player.getCurrentMana()), startX, startY+2*height);
        gc.fillText("Level: " + Integer.toString(player.getLevel()), startX, startY+3*height);
        gc.fillText("Gold: " + Integer.toString(player.getCurrentGold()), startX, startY+4*height);


    }

    private void renderPlayerSprite(GraphicsContext gc) {
        Image playerSprite = player.getObserver().getSprite();

        //Check if player sprite is null
        if(playerSprite == null) {
            return;
        }

        //Rotate 180 degrees so sprite is facing downwards
        rotate(gc, 180, Commons.SCREEN_WIDTH/2 + 150, Commons.SCREEN_HEIGHT*1/4 + 100);

        //Render player sprite
        gc.drawImage(playerSprite, Commons.SCREEN_WIDTH/2, Commons.SCREEN_HEIGHT*1/4 - 50, 300, 300);

        //Set rotate back to normal
        rotate(gc, 0, 0, 0);
    }

    private void rotate(GraphicsContext gc, double angle, double px, double py) {
        Rotate r = new Rotate(angle, px, py);
        gc.setTransform(r.getMxx(), r.getMyx(), r.getMxy(), r.getMyy(), r.getTx(), r.getTy());
    }

    private void renderEquipment(GraphicsContext gc) {
        int startX = Commons.SCREEN_WIDTH / 4 + 20;
        int startY = 2 * Commons.SCREEN_HEIGHT / 3;
        int width = Commons.SCREEN_WIDTH / 6;
        int height = Commons.SCREEN_HEIGHT / 4;

        WeaponItem weaponItem = player.getEquipment().getEquippedWeapon();
        ArmorItem armorItem = player.getEquipment().getEquippedArmor();
        RingItem ringItem = player.getEquipment().getEquippedRing();

        gc.setFill(Color.LIMEGREEN);
        gc.fillRect(startX, startY, width * 3, height);

        gc.setFont(new Font(30.0f).font("System", FontWeight.BOLD, 30.0f));
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(10.0f);

        if(weaponItem == null){
            gc.fillText("No Weapon \nEquipped", startX + width / 6, startY + 2 * height / 5);
        }else{
            if(player.getWeaponItem().getObserver()!= null) {
                Image weapon = player.getWeaponItem().getObserver().getSprite();

                gc.drawImage(weapon, startX + width / 6, startY + 2 * height / 5 - 40, 150, 150);
            }
        }

        if(armorItem == null){
            gc.fillText("No Armor \nEquipped", startX + width + width / 6, startY + 2 * height / 5);
        }else{
            if(player.getArmorItem().getObserver()!= null) {
                Image armor = player.getArmorItem().getObserver().getSprite();

                gc.drawImage(armor, startX + width + width / 6, startY + 2 * height / 5 -50, 150, 150);
            }
        }

        if(ringItem == null){
            gc.fillText("No Ring \nEquipped", startX + 2 * width + width / 6, startY + 2 * height / 5);
        }else{
            if(player.getRingItem().getObserver() != null) {
                Image ring = player.getRingItem().getObserver().getSprite();

                gc.drawImage(ring, startX + 2 * width + width / 6, startY + 2 * height / 5 - 75, 150, 150);
            }
        }

        gc.strokeRect(startX, startY, width, height);
        gc.strokeRect(startX + width, startY, width, height);
        gc.strokeRect(startX + 2 * width, startY, width, height);

        if(selectedX != 0){
            gc.drawImage(selected, startX + (selectedX - 1) * width, startY, width, height);
        }
    }
}
