package Controller.Factories;

import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleDefenseCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.AddHealthCommand;
import Model.Command.EntityCommand.SettableCommand.AddManaCommand;
import Model.Command.EntityCommand.SettableCommand.FreezeEntityCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.Item;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.RingItem;
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
        RemoveHealthCommand command = new RemoveHealthCommand(12);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getOneHandedSkill();
        WeaponItem oneHandedSword = new WeaponItem("One Handed Sword", command, skill, influenceEffect, 10, 0330000000l, 10, 10, 1 );
        oneHandedSword.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setOneHandedSword();

        oneHandedSword.setObserver(itemView);
        oneHandedSword.setPrice(5);

        return oneHandedSword;
    }

    //Instantiates a two handed weapon
    public WeaponItem getTwoHandedSword() {
        RemoveHealthCommand command = new RemoveHealthCommand(20);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getTwoHandedSkill();
        WeaponItem twoHandedSword = new WeaponItem("Two Handed Sword", command, skill, influenceEffect, 20, 0500000000l, 10, 10, 1 );
        twoHandedSword.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setTwoHandedWeapon();

        twoHandedSword.setObserver(itemView);
        twoHandedSword.setPrice(10);

        return twoHandedSword;
    }

    //Instantiates a brawler weapon
    public WeaponItem getBrawlerWeapon() {
        RemoveHealthCommand command = new RemoveHealthCommand(6);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getBrawlerSkill();
        WeaponItem brawlerWeapon = new WeaponItem("Brawler Weapon", command, skill, influenceEffect, 5, 0250000000l, 10, 10, 1 );
        brawlerWeapon.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setBrawlerWeapon();

        brawlerWeapon.setObserver(itemView);
        brawlerWeapon.setPrice(5);

        return brawlerWeapon;
    }

    //Instantiates a staff
    public WeaponItem getStaff(){
        RemoveHealthCommand command = new RemoveHealthCommand(10);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 1, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getStaffSkill();
        WeaponItem staff1 = new WeaponItem("Staff", command, skill, influenceEffect, 8, 0250000000l, 10, 10, 1 );
        staff1.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setStaff();

        staff1.setObserver(itemView);
        staff1.setPrice(12);

        return staff1;
    }

    public WeaponItem getRangedWeapon(){
        RemoveHealthCommand command = new RemoveHealthCommand(15);
        AngularInfluenceEffect influenceEffect = new AngularInfluenceEffect(command, 10, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getRangeSkill();
        WeaponItem bow = new WeaponItem("Shotgun Crossbow", command, skill, influenceEffect, 8, 0250000000l, 10, 10, 1 );
        bow.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setRangedWeapon();

        bow.setObserver(itemView);
        bow.setPrice(10);

        return bow;
    }

    public ConsumableItem getPotion() {
        AddHealthCommand command = new AddHealthCommand(50);
        ConsumableItem potion = new ConsumableItem("Health Potion", command);
        potion.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setPotion();

        potion.setObserver(itemView);
        potion.setPrice(2);

        return potion;
    }

    public ConsumableItem getManaPotion() {
        AddManaCommand command = new AddManaCommand(50);
        ConsumableItem potion = new ConsumableItem("Mana Potion", command);
        potion.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setManaPotion();

        potion.setObserver(itemView);
        potion.setPrice(2);

        return potion;
    }

    public WeaponItem getFreezeBow() {
        FreezeEntityCommand command = new FreezeEntityCommand(levelMessenger);
        LinearInfluenceEffect influenceEffect = new LinearInfluenceEffect(command, 10, 0250000000l, Orientation.NORTH);
        Skill skill = skillsFactory.getRangeSkill();
        WeaponItem bow = new WeaponItem("Freeze Bow", command, skill, influenceEffect, 8, 0250000000l, 10, 10, 1 );
        bow.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setRangedWeapon();

        bow.setObserver(itemView);
        bow.setPrice(20);

        return bow;
    }

    public ArmorItem getLightArmor() {
        ToggleHealthCommand command = new ToggleHealthCommand(5);
        ArmorItem armorItem = new ArmorItem("Light Armor", command, 5);
        armorItem.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setLightArmor();

        armorItem.setObserver(itemView);
        armorItem.setPrice(10);

        return armorItem;
    }

    public ArmorItem getMediumArmor() {
        ToggleHealthCommand command = new ToggleHealthCommand(10);
        ArmorItem armorItem = new ArmorItem("Medium Armor", command, 11);
        armorItem.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setMediumArmor();

        armorItem.setObserver(itemView);
        armorItem.setPrice(23);

        return armorItem;
    }

    public ArmorItem getHeavyArmor() {
        ToggleHealthCommand command = new ToggleHealthCommand(15);
        ArmorItem armorItem = new ArmorItem("Heavy Armor", command, 16);
        armorItem.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setHeavyArmor();

        armorItem.setObserver(itemView);
        armorItem.setPrice(37);

        return armorItem;
    }

    public RingItem getSpeedRing() {
        ToggleSpeedCommand command = new ToggleSpeedCommand(-0500000000);
        RingItem ringItem = new RingItem("Speed Ring", command);
        ringItem.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setSpeedRing();

        ringItem.setObserver(itemView);
        ringItem.setPrice(55);

        return ringItem;

    }

    public RingItem getHealthRing() {
        ToggleHealthCommand command = new ToggleHealthCommand(50);
        RingItem ringItem = new RingItem("Health Ring", command);
        ringItem.setCurrentLevelMessenger(levelMessenger);

        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setHealthRing();

        ringItem.setObserver(itemView);
        ringItem.setPrice(24);

        return ringItem;
    }


}
