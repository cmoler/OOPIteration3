package Controller.Factories;

import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.SettableCommand.RemoveHealthCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.Item.Item;
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
        Skill skill = new Skill("WeaponSkill", influenceEffect, command, new SendInfluenceEffectCommand(levelMessenger), 10, 10);
        WeaponItem oneHandedSword = new WeaponItem("One Handed Sword", command, skill, influenceEffect, 10, 10, 10, 10, 1 );
        oneHandedSword.setCurrentLevelMessenger(levelMessenger);
        ItemView itemView = new ItemView(new Point3D(0, 0, 0));
        itemView.setOneHandedSword();
        oneHandedSword.setObserver(itemView);
        return oneHandedSword;
    }
}
