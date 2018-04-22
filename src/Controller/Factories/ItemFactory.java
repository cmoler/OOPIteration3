package Controller.Factories;

import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleDefenseCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.AddManaCommand;
import Model.Command.EntityCommand.SettableCommand.FreezeEntityCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.Item;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.LevelMessenger;
import View.LevelView.InfluenceEffectView;
import View.LevelView.ItemView;
import javafx.geometry.Point3D;

public class ItemFactory {
    private SkillsFactory skillsFactory;
    private LevelMessenger levelMessenger;
    public ItemFactory(SkillsFactory skillsFactory, LevelMessenger levelMessenger) {
        this.skillsFactory = skillsFactory;
        this.levelMessenger = levelMessenger;
    }

    //Instantiates a one handed weapon
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

    //Instantiates a two handed weapon
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

    //Instantiates a brawler weapon
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

    //Instantiates a staff
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
        AddManaCommand command = new AddManaCommand(50);

        ConsumableItem potion = new ConsumableItem("Mana Potion", command);
        potion.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setManaPotion();
        potion.setObserver(itemView);
        return potion;
    }

    public WeaponItem getFreezeBow() {
        FreezeEntityCommand command = new FreezeEntityCommand(levelMessenger);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 10, 10, Orientation.NORTH);

        Skill skill = skillsFactory.getRangeSkill();

        WeaponItem staff1 = new WeaponItem("Freeze Bow", command, skill, influenceEffect, 8, 5, 10, 10, 1 );
        staff1.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setRangedWeapon();
        staff1.setObserver(itemView);
        return staff1;
    }

    public ArmorItem getLightArmor() {
        ToggleDefenseCommand command = new ToggleDefenseCommand(5);
        ArmorItem armorItem = new ArmorItem("Light Armor", command, 5);
        armorItem.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setLightArmor();
        armorItem.setObserver(itemView);
        return armorItem;
    }

    public ArmorItem getMediumArmor() {
        ToggleDefenseCommand command = new ToggleDefenseCommand(10);
        ArmorItem armorItem = new ArmorItem("Medium Armor", command, 10);
        armorItem.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setMediumArmor();
        armorItem.setObserver(itemView);
        return armorItem;
    }

    public ArmorItem getHeavyArmor() {
        ToggleDefenseCommand command = new ToggleDefenseCommand(15);
        ArmorItem armorItem = new ArmorItem("Heavy Armor", command, 15);
        armorItem.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setHeavyArmor();
        armorItem.setObserver(itemView);
        return armorItem;
    }
}
