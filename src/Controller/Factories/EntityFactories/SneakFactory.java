package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;

public class SneakFactory extends EntityFactory {

    public SneakFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity sneak = new Entity();

        sneak.addWeaponSkills(getSkillsFactory().getRangeSkill());

        sneak.addNonWeaponSkills(getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds(),
                getSkillsFactory().getPickpocket(),
                getSkillsFactory().getSneakSkill(),
                getSkillsFactory().DisarmTrapSkill()
        );

        return sneak;
    }
}
