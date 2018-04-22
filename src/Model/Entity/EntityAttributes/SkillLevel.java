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
        this.maxSkillLevel = 99;
    }

    public void increaseSkillLevel() {
        if(skillLevel + 1 > maxSkillLevel) {
            skillLevel = maxSkillLevel;
        }

        else {
            skillLevel++;
        }
    }

    public void decreaseSkillLevel() {
        if(skillLevel - 1 < 0) {
            skillLevel = 0;
        }

        else {
            skillLevel--;
        }
    }

    public int getSkillLevel() {
        return skillLevel;
    }

    public void setSkillLevel(int skillLevel) {
        if(skillLevel < maxSkillLevel) {
            this.skillLevel = skillLevel;
        } else {
            this.skillLevel = maxSkillLevel;
        }
    }

    public boolean isMaxLevel(){
        return skillLevel >= maxSkillLevel;
    }
    
}
