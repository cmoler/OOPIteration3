package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

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

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView sneakView = new EntityView(entity, new Point3D(0,0,0));
    }
}
