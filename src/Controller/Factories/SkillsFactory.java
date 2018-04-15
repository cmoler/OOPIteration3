package Controller.Factories;

import Model.Command.LevelCommand.PickPocketCommand;
import Model.Entity.EntityAttributes.Skill;
import Model.Level.LevelMessenger;

// TODO: Not sure what to use for bind wounds command and barter command

public class SkillsFactory {

    private LevelMessenger levelMessenger;
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
    }

    public Skill getObserveSkill() {
        if(observeSkill == null) {
            observeSkill = new Skill("Observe", null, null, null, 1, 1);
        }

        return observeSkill;
    }

    public Skill getBindWounds() {
        if(bindWounds == null) {
            bindWounds = new Skill("Bind Wounds", null, null, null, 1, 1);
        }

        return bindWounds;
    }

    public Skill getBargainSkill() {
        if(bargainSkill == null) {
            bargainSkill = new Skill("Bargain", null, null, null, 1, 1);
        }

        return bargainSkill;
    }

    public Skill getOneHandedSkill() {
        if(oneHandedSkill == null) {
            oneHandedSkill = new Skill("One-Handed", null, null, null, 1, 1);
        }

        return oneHandedSkill;
    }

    public Skill getTwoHandedSkill() {
        if(twoHandedSkill == null) {
            twoHandedSkill = new Skill("Two-Handed", null, null, null, 1, 1);
        }

        return twoHandedSkill;
    }

    public Skill getBrawlerSkill() {
        if(brawlerSkill == null) {
            brawlerSkill = new Skill("Brawler", null, null, null, 1, 1);
        }

        return brawlerSkill;
    }

    public Skill getEnchantSkill() {
        if(enchantSkill == null) {
            enchantSkill = new Skill("Enchant", null, null, null, 1, 1);
        }

        return enchantSkill;
    }

    public Skill getBoonSkill() {
        if(boonSkill == null) {
            boonSkill = new Skill("Boon", null, null, null, 1, 1);
        }

        return boonSkill;
    }

    public Skill getBaneSkill() {
        if(baneSkill == null) {
            baneSkill = new Skill("Bane", null, null, null, 1, 1);
        }

        return baneSkill;
    }

    public Skill getStaffSkill() {
        if(staffSkill == null) {
            staffSkill = new Skill("Staff", null, null, null, 1, 1);
        }

        return staffSkill;
    }

    public Skill getSneakSkill() {
        if(sneakSkill == null) {
            sneakSkill = new Skill("Sneak", null, null, null, 1, 1);
        }

        return sneakSkill;
    }

    public Skill getDisarmAndRemoveSkill() {
        if(disarmAndRemoveSkill == null) {
            disarmAndRemoveSkill = new Skill("Disarm and Remove Trap", null, null, null, 1, 1);
        }

        return disarmAndRemoveSkill;
    }

    public Skill getPickpocket() {
        if(pickpocketSkill == null) {
            pickpocketSkill = new Skill("Pickpocket", null, new PickPocketCommand(levelMessenger), null, 1, 1);
        }

        return pickpocketSkill;
    }

    public Skill getRangeSkill() {
        if(rangeSkill == null) {
            rangeSkill = new Skill("Range", null, null, null, 1, 1);
        }

        return rangeSkill;
    }
}
