package Controller.Factories;

import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Level.LevelMessenger;

public class SkillsFactory {

    private LevelMessenger levelMessenger;
    private SendInfluenceEffectCommand sendInfluenceEffectCommand;

    private Skill observeSkill;
    private Skill bindWounds;
    private Skill bargainSkill;

    private Skill oneHandedSkill;
    private Skill twoHandedSkill;
    private Skill brawlerSkill;

    private Skill enchantSkill;
    private Skill boonSkill;
    private Skill baneSkill;
    private Skill staffSkill;

    private Skill sneakSkill;
    private Skill disarmAndRemoveSkill;
    private Skill pickpocketSkill;
    private Skill rangeSkill;

    public SkillsFactory(LevelMessenger levelMessenger) {
        this.levelMessenger = levelMessenger;
        this.sendInfluenceEffectCommand = new SendInfluenceEffectCommand(levelMessenger);
    }

    public Skill getObserveSkill() {
        if(observeSkill == null) {
            ObserveEntityCommand observeEntityCommand = new ObserveEntityCommand(levelMessenger);

            RadialInfluenceEffect radialInfluenceEffect = new RadialInfluenceEffect(observeEntityCommand, 5, 0500000000L, Orientation.NORTH);

            observeSkill = new Skill("Observe", radialInfluenceEffect, observeEntityCommand, sendInfluenceEffectCommand, 1, 100);
        }

        return observeSkill;
    }

    public Skill getBindWounds() {
        if(bindWounds == null) {
            AddHealthCommand addHealthCommand = new AddHealthCommand(10);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(addHealthCommand, 0, 0500000000L, Orientation.NORTH);

            bindWounds = new Skill("Bind Wounds", linearInfluenceEffect, addHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return bindWounds;
    }

    public Skill getBargainSkill() {
        if(bargainSkill == null) {
            BarterCommand bargainCommand = new BarterCommand(levelMessenger);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(bargainCommand, 0, 0500000000L, Orientation.NORTH);

            bargainSkill = new Skill("Bargain", linearInfluenceEffect, bargainCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return bargainSkill;
    }

    public Skill getOneHandedSkill() {
        if(oneHandedSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 1, 0500000000L, Orientation.NORTH);

            oneHandedSkill = new Skill("One-Handed", linearInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return oneHandedSkill;
    }

    public Skill getTwoHandedSkill() {
        if(twoHandedSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 2, 0500000000L, Orientation.NORTH);

            twoHandedSkill = new Skill("Two-Handed", linearInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return twoHandedSkill;
    }

    public Skill getBrawlerSkill() {
        if(brawlerSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 1, 0500000000L, Orientation.NORTH);

            brawlerSkill = new Skill("Brawler", linearInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return brawlerSkill;
    }

    public Skill getEnchantSkill() {
        if(enchantSkill == null) {
            FreezeEntityCommand freezeEntityCommand = new FreezeEntityCommand(levelMessenger);
            AngularInfluenceEffect angularInfluenceEffect = new AngularInfluenceEffect(freezeEntityCommand, 4, 0500000000L, Orientation.NORTH);

            enchantSkill = new Skill("Enchant", angularInfluenceEffect, freezeEntityCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return enchantSkill;
    }

    public Skill getBoonSkill() {
        if(boonSkill == null) {
            AddHealthCommand addHealthCommand = new AddHealthCommand(10);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(addHealthCommand, 0, 0500000000L, Orientation.NORTH);

            boonSkill = new Skill("Boon", linearInfluenceEffect, addHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return boonSkill;
    }

    public Skill getBaneSkill() {
        if(baneSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            AngularInfluenceEffect angularInfluenceEffect = new AngularInfluenceEffect(removeHealthCommand, 4, 0500000000L, Orientation.NORTH);

            baneSkill = new Skill("Bane", angularInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return baneSkill;
    }

    public Skill getStaffSkill() {
        if(staffSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 1, 0500000000L, Orientation.NORTH);

            staffSkill = new Skill("Staff", linearInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return staffSkill;
    }

    public Skill getSneakSkill() {
        if (sneakSkill == null) {
            ToggleSneaking sneakCommand = new ToggleSneaking(5);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(sneakCommand, 0, 0500000000L, Orientation.NORTH);

            sneakSkill = new Skill("Sneak", linearInfluenceEffect, sneakCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return sneakSkill;
    }

    public Skill DisarmTrapSkill() {
        if(disarmAndRemoveSkill == null) {
            DisarmTrapCommand disarmTrapCommand = new DisarmTrapCommand(levelMessenger);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(disarmTrapCommand, 0, 0500000000L, Orientation.NORTH);

            disarmAndRemoveSkill = new Skill("Detect and Remove Trap", linearInfluenceEffect, disarmTrapCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return disarmAndRemoveSkill;
    }

    public Skill getPickpocket() {
        if(pickpocketSkill == null) {
            PickPocketCommand pickPocketCommand = new PickPocketCommand(levelMessenger);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(pickPocketCommand, 0, 0500000000L, Orientation.NORTH);

            pickpocketSkill = new Skill("Pickpocket", linearInfluenceEffect, pickPocketCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return pickpocketSkill;
    }

    public Skill getRangeSkill() {
        if(rangeSkill == null) {
            RemoveHealthCommand removeHealthCommand = new RemoveHealthCommand(15);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(removeHealthCommand, 7, 0500000000L, Orientation.NORTH);

            rangeSkill = new Skill("Range", linearInfluenceEffect, removeHealthCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return rangeSkill;
    }
}
