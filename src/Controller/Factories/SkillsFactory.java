package Controller.Factories;

import Model.Command.EntityCommand.SettableCommand.DisarmTrapCommand;
import Model.Command.EntityCommand.SettableCommand.PickPocketCommand;
import Model.Command.EntityCommand.NonSettableCommand.SendInfluenceEffectCommand;
import Model.Entity.EntityAttributes.Orientation;
import Model.Entity.EntityAttributes.Skill;
import Model.InfluenceEffect.LinearInfluenceEffect;
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

    // TODO: parameterize allllll the Skills with alllllll the stuff

    public Skill getObserveSkill() { // TODO: me
        if(observeSkill == null) {
            observeSkill = new Skill("Observe", null, null, null, 1, 1);
        }

        return observeSkill;
    }

    public Skill getBindWounds() {  // TODO: me 2
        if(bindWounds == null) {
            bindWounds = new Skill("Bind Wounds", null, null, null, 1, 1);
        }

        return bindWounds;
    }

    public Skill getBargainSkill() {  // TODO: me 2 2
        if(bargainSkill == null) {
            bargainSkill = new Skill("Bargain", null, null, null, 1, 1);
        }

        return bargainSkill;
    }

    public Skill getOneHandedSkill() {  // TODO: me 2 2 2
        if(oneHandedSkill == null) {
            oneHandedSkill = new Skill("One-Handed", null, null, null, 1, 1);
        }

        return oneHandedSkill;
    }

    public Skill getTwoHandedSkill() {  // TODO: me 2 2 2 2
        if(twoHandedSkill == null) {
            twoHandedSkill = new Skill("Two-Handed", null, null, null, 1, 1);
        }

        return twoHandedSkill;
    }

    public Skill getBrawlerSkill() { // TODO: me 2 2 2 2 2
        if(brawlerSkill == null) {
            brawlerSkill = new Skill("Brawler", null, null, null, 1, 1);
        }

        return brawlerSkill;
    }

    public Skill getEnchantSkill() { // TODO: me 2 2 2 2 2 2
        if(enchantSkill == null) {
            enchantSkill = new Skill("Enchant", null, null, null, 1, 1);
        }

        return enchantSkill;
    }

    public Skill getBoonSkill() { // TODO: me 2 2 2 2 2 2 2
        if(boonSkill == null) {
            boonSkill = new Skill("Boon", null, null, null, 1, 1);
        }

        return boonSkill;
    }

    public Skill getBaneSkill() { // TODO: me 2 2 2 2 2 2 2 2
        if(baneSkill == null) {
            baneSkill = new Skill("Bane", null, null, null, 1, 1);
        }

        return baneSkill;
    }

    public Skill getStaffSkill() { // TODO: me 2 2 2 2 2 2 2 2 2
        if(staffSkill == null) {
            staffSkill = new Skill("Staff", null, null, null, 1, 1);
        }

        return staffSkill;
    }

    public Skill getSneakSkill() { // TODO: me 2 2 2 2 2 2 2 2 2 2
        if (sneakSkill == null) {
            sneakSkill = new Skill("Sneak", null, null, null, 1, 1);
        }

        return sneakSkill;
    }

    public Skill DisarmTrapSkill() {
        if(disarmAndRemoveSkill == null) {
            DisarmTrapCommand disarmTrapCommand = new DisarmTrapCommand(levelMessenger);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(disarmTrapCommand, 0, 1, Orientation.NORTH);

            disarmAndRemoveSkill = new Skill("Detect and Remove Trap", linearInfluenceEffect, disarmTrapCommand, sendInfluenceEffectCommand, 1, 1);
        }

        return disarmAndRemoveSkill;
    }

    public Skill getPickpocket() {
        if(pickpocketSkill == null) {

            PickPocketCommand pickPocketCommand = new PickPocketCommand(levelMessenger);
            LinearInfluenceEffect linearInfluenceEffect = new LinearInfluenceEffect(pickPocketCommand, 0, 1, Orientation.NORTH);

            pickpocketSkill = new Skill("Pickpocket", linearInfluenceEffect, new PickPocketCommand(levelMessenger), sendInfluenceEffectCommand, 1, 1);
        }

        return pickpocketSkill;
    }

    public Skill getRangeSkill() { // TODO: me 2 2 2 2 2 2 2 2 2 2 2
        if(rangeSkill == null) {
            rangeSkill = new Skill("Range", null, null, null, 1, 1);
        }

        return rangeSkill;
    }
}
