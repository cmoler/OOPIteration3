package Controller.Factories;

import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.Item;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.LevelMessenger;
import View.LevelView.ItemView;
import javafx.geometry.Point3D;

public class ItemFactory {
    private SkillsFactory skillsFactory;
    private LevelMessenger levelMessenger;
    public ItemFactory(SkillsFactory skillsFactory, LevelMessenger levelMessenger) {
        this.skillsFactory = skillsFactory;
        this.levelMessenger = levelMessenger;
    }

    public WeaponItem getOneHandedSword() {
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 10, Orientation.NORTH);
        Skill skill = skillsFactory.getOneHandedSkill();
        WeaponItem oneHandedSword = new WeaponItem("One Handed Sword", command, skill, influenceEffect, 10, 10, 10, 10, 1 );
        oneHandedSword.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setOneHandedSword();

        oneHandedSword.setObserver(itemView);
        return oneHandedSword;
    }

    public WeaponItem getTwoHandedSword() {
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 10, Orientation.NORTH);
        Skill skill = skillsFactory.getTwoHandedSkill();
        WeaponItem twoHandedSword = new WeaponItem("Two Handed Sword", command, skill, influenceEffect, 20, 5, 10, 10, 1 );
        twoHandedSword.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setTwoHandedWeapon();
        twoHandedSword.setObserver(itemView);
        return twoHandedSword;
    }

    public WeaponItem getBrawlerWeapon() {
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 10, Orientation.NORTH);
        Skill skill = skillsFactory.getBrawlerSkill();
        WeaponItem brawlerWeapon = new WeaponItem("Brawler Weapon", command, skill, influenceEffect, 5, 20, 10, 10, 1 );
        brawlerWeapon.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setBrawlerWeapon();
        brawlerWeapon.setObserver(itemView);
        return brawlerWeapon;
    }

    public WeaponItem getStaff(){
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 10, Orientation.NORTH);
        Skill skill = skillsFactory.getStaffSkill();
        WeaponItem staff1 = new WeaponItem("Staff", command, skill, influenceEffect, 8, 5, 10, 10, 1 );
        staff1.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setStaff();
        staff1.setObserver(itemView);
        return staff1;
    }

    public WeaponItem getRangedWeapon(){
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 10, 10, Orientation.NORTH);
        Skill skill = skillsFactory.getRangeSkill();
        WeaponItem staff1 = new WeaponItem("Ranged Weapon", command, skill, influenceEffect, 8, 5, 10, 10, 1 );
        staff1.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setRangedWeapon();
        staff1.setObserver(itemView);
        return staff1;
    }

    public ConsumableItem getPotion() {
        AddHealthCommand command = new AddHealthCommand(50);
        ConsumableItem potion = new ConsumableItem("Potion", command);
        potion.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setPotion();
        potion.setObserver(itemView);
        return potion;
    }

    public ConsumableItem getManaPotion() {
        AddHealthCommand command = new AddHealthCommand(50);

        ConsumableItem potion = new ConsumableItem("Potion", command);
        potion.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setPotion();
        potion.setObserver(itemView);
        return potion;
    }

}
