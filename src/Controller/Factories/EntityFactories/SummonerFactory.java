package Controller.Factories.EntityFactories;

import Controller.Factories.EntityFactories.EntityFactory;
import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;
import View.LevelView.EntityView;
import javafx.geometry.Point3D;

public class SummonerFactory extends EntityFactory {

    public SummonerFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity summoner = new Entity();

        summoner.addWeaponSkills(
                getSkillsFactory().getStaffSkill(),
                getSkillsFactory().getBaneSkill(),
                getSkillsFactory().getBoonSkill(),
                getSkillsFactory().getEnchantSkill()
        );

        summoner.addNonWeaponSkills(
                getSkillsFactory().getBargainSkill(),
                getSkillsFactory().getObserveSkill(),
                getSkillsFactory().getBindWounds()
        );

        return summoner;
    }

    public void buildEntitySprite(Entity entity) { // TODO: subclass entityView to make monster sprite
        EntityView summonerView = new EntityView(entity, new Point3D(0,0,0));
    }
}
