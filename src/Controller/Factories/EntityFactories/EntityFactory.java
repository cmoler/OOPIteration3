package Controller.Factories.EntityFactories;

import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;

public abstract class EntityFactory {

    private SkillsFactory skillsFactory;

    public EntityFactory(SkillsFactory skillsFactory) {
        this.skillsFactory = skillsFactory;
    }

    public abstract Entity buildEntity();

    public abstract void buildEntitySprite(Entity entity);

    protected SkillsFactory getSkillsFactory() {
        return skillsFactory;
    }
}