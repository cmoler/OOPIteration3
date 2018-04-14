package Controller.Factories;

import Model.Entity.Entity;

public class SummonerFactory extends EntityFactory {

    public SummonerFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity summoner = new Entity();
        summoner.addSkillsToMap(
                getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds(),
                getSkillsFactory().getBaneSkill(),
                getSkillsFactory().getBoonSkill(),
                getSkillsFactory().getEnchantSkill(),
                getSkillsFactory().getStaffSkill()
        );

        summoner.addWeaponSkills(getSkillsFactory().getStaffSkill());

        summoner.addNonWeaponSkills(getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds(),
                getSkillsFactory().getBaneSkill(),
                getSkillsFactory().getBoonSkill(),
                getSkillsFactory().getEnchantSkill()
        );

        return summoner;
    }
}
