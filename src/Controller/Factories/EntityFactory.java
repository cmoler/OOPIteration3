package Controller.Factories;

import Model.Entity.Entity;

public abstract class EntityFactory {

    private SkillsFactory skillsFactory;

    public EntityFactory(SkillsFactory skillsFactory) {
        this.skillsFactory = skillsFactory;
    }

    public abstract Entity buildEntity();

    protected SkillsFactory getSkillsFactory() {
        return skillsFactory;
    }
}
