package Controller.Factories;

import Model.Entity.Entity;

public class SmasherFactory extends EntityFactory {

    public SmasherFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity smasher = new Entity();
        smasher.addSkillsToMap(
                getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds(),
                getSkillsFactory().getOneHandedSkill(),
                getSkillsFactory().getTwoHandedSkill(),
                getSkillsFactory().getBrawlerSkill()
        );

        smasher.addWeaponSkills(getSkillsFactory().getOneHandedSkill(),
                getSkillsFactory().getTwoHandedSkill(),
                getSkillsFactory().getBrawlerSkill()
        );

        smasher.addNonWeaponSkills(getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds()
        );

        return smasher;
    }
}
