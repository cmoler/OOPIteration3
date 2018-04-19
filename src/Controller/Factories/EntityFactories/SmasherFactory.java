package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;

public class SmasherFactory extends EntityFactory {

    public SmasherFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity smasher = new Entity();

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
