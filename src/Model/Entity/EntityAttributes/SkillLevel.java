package Model.Entity.EntityAttributes;

public class SkillLevel {

    private int skillLevel;
    private int maxSkillLevel;

    public SkillLevel() {
        skillLevel = 1;
        maxSkillLevel = 99;
    }

    public SkillLevel(int skillLevel) {
        this.skillLevel = skillLevel;
    }

    public void increaseSkillLevel() {
        if(skillLevel + 1 > maxSkillLevel) {
            skillLevel = maxSkillLevel;
        }

        else {
            skillLevel++;
        }
    }

    public int getSkillLevel() {
        return skillLevel;
    }
}
