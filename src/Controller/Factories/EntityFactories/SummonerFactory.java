package Controller.Factories.EntityFactories;

import Controller.Factories.SkillsFactory;
import Model.Entity.Entity;

import Model.Entity.EntityAttributes.SightRadius;
import View.LevelView.EntityView.SummonerView;
import javafx.geometry.Point3D;


public class SummonerFactory extends EntityFactory {

    public SummonerFactory(SkillsFactory skillsFactory) {
        super(skillsFactory);
    }

    @Override
    public Entity buildEntity() {
        Entity summoner = new Entity();
        summoner.setSpeed(0640000000L);
        summoner.setSightRadius(new SightRadius(3));

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

        summoner.setName("summoner");

        return summoner;
    }


    public void buildEntitySprite(Entity entity) {
        SummonerView summonerView = new SummonerView(entity, new Point3D(0,0,0));
    }

}
