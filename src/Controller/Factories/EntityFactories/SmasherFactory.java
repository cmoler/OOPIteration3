package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

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

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView smasherView = new EntityView(entity, new Point3D(0,0,0));
    }
}
