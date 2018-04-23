package Controller.Visitor;

import Model.AI.*;
import Model.AI.PetAI.PetStates.CombatPetState;
import Model.AI.PetAI.PetStates.GeneralPetState;
import Model.AI.PetAI.PetStates.ItemPetState;
import Model.AI.PetAI.PetStates.PassivePetState;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.EntityCommand.NonSettableCommand.*;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.*;
import Model.InfluenceEffect.InfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.*;
import Model.Level.*;
import Model.Utility.BidiMap;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class SavingVisitor implements Visitor {

    private BufferedWriter writer;
    private StringBuffer valueNode = new StringBuffer("<VALUE>");
    private StringBuffer currentLevel = new StringBuffer("<CURRENTLEVEL>");
    private StringBuffer levelString = new StringBuffer("<LEVEL");
    private StringBuffer levelList = new StringBuffer("<LEVELLIST>");
    private StringBuffer gameModelString = new StringBuffer("<GAMEMODEL>\n");
    private StringBuffer entityNode = new StringBuffer("<ENTITY>");

    public SavingVisitor(String fileName) throws IOException {
        writer = new BufferedWriter(new FileWriter(fileName));
    }

    private StringBuffer keyToString(Point3D point) {
        StringBuffer pointStr = new StringBuffer();
        pointStr.append((int)point.getX());
        pointStr.append(",");
        pointStr.append((int)point.getY());
        pointStr.append(",");
        pointStr.append((int)point.getZ());
        return pointStr;
    }

    private StringBuffer vecToString(Vec3d vec) {
        StringBuffer pointStr = new StringBuffer();
        pointStr.append((int)vec.x);
        pointStr.append(",");
        pointStr.append((int)vec.y);
        pointStr.append(",");
        pointStr.append((int)vec.z);
        return pointStr;
    }

    public void visitGameModel(GameModel gameModel) {
        try {
            writer.write(gameModelString.toString());

            try {
                if(gameModel.hasCurrentLevel()) {
                    saveCurrentLevel(gameModel.getCurrentLevel());
                }

                if(gameModel.hasLevels()) {
                    saveLevelList(gameModel.getLevels());
                }

                if(gameModel.hasPlayer()) {
                    visitPlayerEntity(gameModel.getPlayer());
                }

                if(!gameModel.isTeleportQueueEmpty()) {
                    this.valueNode = new StringBuffer("<TELEPORTQUEUE id=" + "\"progress" + "\">");
                    visitTelportQueue(gameModel.getTeleportQueue());
                }

                if(!gameModel.isFailedQueueEmpty()) {
                    this.valueNode = new StringBuffer("<TELEPORTQUEUE id=" + "\"failed" + "\">");
                    visitTelportQueue(gameModel.getFailedQueue());
                }

                if(gameModel.hasAI()) {
                    this.valueNode = new StringBuffer("<AICONTROLLERS>");
                    this.valueNode.append("\n");
                    this.valueNode.append("\t");
                    visitAIMap(gameModel.getAiMap());
                    this.valueNode.append("</AICONTROLLERS>");
                    writer.write(this.valueNode.toString());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            writer.write("</GAMEMODEL>");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void visitAIMap(Map<Level, List<AIController>> aiMap) {
        aiMap.entrySet().forEach(entry -> {
            Level level = entry.getKey();
            entry.getValue().forEach(aiController -> {
                StringBuffer controller = new StringBuffer("<" + aiController.getClass().getSimpleName()
                        + " levelRef=" + "\"" + level.toString() + "\""
                        + " aiState=" + "\"" + aiController.getActiveState().getClass().getSimpleName() + "\""
                        + " entityRef=" + "\"" + aiController.getEntity().toString() + "\""
                        + " reference=" + "\"" + aiController.toString() + "\">");
                this.valueNode.append(controller);
                aiController.accept(this);
                this.valueNode.append("</" + aiController.getClass().getSimpleName() + ">");
            });
        });
    }

    private void visitTelportQueue(Queue<GameModel.TeleportTuple> teleportQueue) throws IOException {
        this.valueNode.append("\n");

        for(GameModel.TeleportTuple teleportTuple: teleportQueue) {
            processTeleportTuple(teleportTuple);
        }
        this.valueNode.append("</TELEPORTQUEUE>");
        writer.append(this.valueNode.toString());
    }

    private void processTeleportTuple(GameModel.TeleportTuple teleportTuple) {
        StringBuffer point = keyToString(teleportTuple.getDestinationPoint());
        StringBuffer tupleString = new StringBuffer("<" + teleportTuple.getClass().getSimpleName()
                + " levelReference=" + "\"" + teleportTuple.entityToString() + "\""
                + " entityReference=" + "\"" + teleportTuple.levelToString() + "\""
                + " point=" + "\"" + point + "\""
                + " reference=" + "\"" + teleportTuple.toString() + "\"/>");
        this.valueNode.append(tupleString);
        this.valueNode.append("\n");
    }

    public void visitLevel(Level level) {
        Map<Point3D, Terrain> terrainLocations = level.getTerrainMap();
        Map<Point3D, Item> itemLocations = level.getItemMap();
        Map<Point3D, Obstacle> obstacleLocations = level.getObstacleMap();
        BidiMap<Point3D, Entity> entityLocations = level.getEntityMap();
        Map<Point3D, AreaEffect> areaEffectLocations = level.getAreaEffectMap();
        Map<Point3D, Trap> trapLocations = level.getTrapMap();
        Map<Point3D, River> riverLocations = level.getRiverMap();
        Map<Point3D, Mount> mountLocations = level.getMountMap();
        Map<Point3D, InfluenceEffect> influenceEffectLocations = level.getInfluenceEffectMap();
        Map<Point3D, Decal> decalLocations = level.getDecalMap(); // TODO: save decal locations and decal type

        levelString.append(" reference=" + "\"" + level.toString() + "\">");
        try {
            levelString.append("\n");
            levelString.append(processTerrains(terrainLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processAreaEffects(areaEffectLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processInfluenceEffects(influenceEffectLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processItems(itemLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processTraps(trapLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processObstacles(obstacleLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processRivers(riverLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processMounts(mountLocations));
            levelString.append("\n");

            levelString.append("\n");
            levelString.append(processEntities(entityLocations));
            levelString.append("\n");


            levelString.append("</LEVEL>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private StringBuffer processTerrains(Map<Point3D, Terrain> terrainLocations) throws IOException {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"TERRAIN\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, Terrain> entry: terrainLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");
            StringBuffer value = new StringBuffer("<VALUE value=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            value.append("\"");
            value.append(entry.getValue());
            value.append("\"");
            value.append("/>");
            levelMapOpen.append(value);
            levelMapOpen.append("\n");
        }

        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processAreaEffects(Map<Point3D, AreaEffect> areaEffectLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"AREAEFFECT\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, AreaEffect> entry: areaEffectLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processInfluenceEffects(Map<Point3D, InfluenceEffect> influenceEffectLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"INFLUENCEEFFECT\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, InfluenceEffect> entry: influenceEffectLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processItems(Map<Point3D, Item> itemLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"ITEM\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, Item> entry: itemLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processTraps(Map<Point3D, Trap> trapLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"TRAP\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, Trap> entry: trapLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processObstacles(Map<Point3D, Obstacle> obstacleLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"OBSTACLE\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, Obstacle> entry: obstacleLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processRivers(Map<Point3D, River> riverLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"RIVER\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, River> entry: riverLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processMounts(Map<Point3D, Mount> mountLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"MOUNT\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Map.Entry<Point3D, Mount> entry: mountLocations.entrySet()) {
            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(entry.getKey()));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entry.getValue().accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    private StringBuffer processEntities(BidiMap<Point3D, Entity> entityLocations) {
        StringBuffer levelMapOpen = new StringBuffer("<LEVELMAP id=\"ENTITY\">");
        StringBuffer levelMapClosed = new StringBuffer("</LEVELMAP>");

        for(Entity entity: entityLocations.getValueList()) {
            Point3D point = entityLocations.getKeyFromValue(entity);

            StringBuffer key = new StringBuffer("<KEY key=");

            key.append("\"");
            key.append(keyToString(point));
            key.append("\"");
            key.append("/>");

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");
            levelMapOpen.append(key);

            levelMapOpen.append("\n");
            levelMapOpen.append("\t");

            entity.accept(this);

            this.valueNode.append("</VALUE>");
            levelMapOpen.append(this.valueNode);
            this.valueNode = new StringBuffer("<VALUE>");
        }

        levelMapOpen.append("\n");
        levelMapOpen.append(levelMapClosed);
        return levelMapOpen;
    }

    public void visitEntity(Entity entity) {
        StringBuffer entityString = new StringBuffer("<" + entity.getClass().getSimpleName()
                + " velocity=" + "\"" + vecToString(entity.getVelocity()) + "\""
                + " noiseLevel=" + "\"" + entity.getNoise() + "\""
                + " sightRadius=" + "\"" + entity.getSight() + "\""
                + " level=" + "\"" + entity.getLevel() + "\""
                + " experience=" + "\"" + entity.getExperience() + "\""
                + " experienceToNextLevel=" + "\"" + entity.getExperienceToNextLevel() + "\""
                + " currentHealth=" + "\"" + entity.getCurrentHealth() + "\""
                + " maxHealth=" + "\"" + entity.getMaxHealth() + "\""
                + " manaPoints=" + "\"" + entity.getManaPoints() + "\""
                + " maxMana=" + "\"" + entity.getMaxMana() + "\""
                + " regenRate=" + "\"" + entity.getManaRegenRate() + "\""
                + " speed=" + "\"" + entity.getSpeed() + "\""
                + " goldAmount=" + "\"" + entity.getGold() + "\""
                + " maxGold=" + "\"" + entity.getMaxGold() + "\""
                + " attackPoints=" + "\"" + entity.getAttackPoints() + "\""
                + " attackModifier=" + "\"" + entity.getAttackModifier() + "\""
                + " defensePoints=" + "\"" + entity.getDefensePoints() + "\""
                + " defenseModifier=" + "\"" + entity.getDefenseModifier() + "\""
                + " orientation=" + "\"" + entity.getOrientation() + "\""
                + " moveable=" + "\"" + entity.isMoveable() + "\""
                + " name=" + "\"" + entity.getName() + "\""
                + " reference=" + "\"" + entity.toString() + "\"" + ">");

        entityString.append("\n");
        entityString.append("\t");
        entityString.append("<WEAPONSKILLS>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(entityString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");

        for(Skill weaponSkill: entity.getWeaponSkills()) {
            processSkill(weaponSkill, entity.getSkillLevel(weaponSkill));
        }

        this.valueNode.append("\n");
        this.valueNode.append("</WEAPONSKILLS>");
        this.valueNode.append("\n");

        this.valueNode.append("<NONWEAPONSKILLS>");
        for(Skill nonWeaponSkill: entity.getNonWeaponSkills()) {
            entityString.append("\t");
            entityString.append("\n");
            processSkill(nonWeaponSkill, entity.getSkillLevel(nonWeaponSkill));
        }

        this.valueNode.append("\n");
        this.valueNode.append("</NONWEAPONSKILLS>");
        this.valueNode.append("\n");

        this.valueNode.append("<TERRAINLIST>");
        for(Terrain terrain: entity.getPassableTerrains()) {
            this.valueNode.append("\t");
            this.valueNode.append("\n");
            this.valueNode.append("<TERRAIN value=\"");
            this.valueNode.append(terrain.toString());
            this.valueNode.append("\"/>");
        }

        this.valueNode.append("\n");
        this.valueNode.append("</TERRAINLIST>");
        this.valueNode.append("\n");

        if(entity.isMounted()) {
            visitMount(entity.getMount());
        }

        this.valueNode.append("\n");
        visitEquipment(entity.getEquipment());
        this.valueNode.append("\n");

        visitInventory(entity.getInventory());
        this.valueNode.append("\n");

        visitItemHotBar(entity.getItemHotBar());
        this.valueNode.append("\n");

        this.valueNode.append("<FRIENDLIST>");
        this.valueNode.append("\n");
        for(Entity friend: entity.getFriendlyList()) {
            this.valueNode.append("<FRIEND entityReference=" + "\"" + friend.toString() + "\"/>");
        }
        this.valueNode.append("</FRIENDLIST>");

        this.valueNode.append("\n");
        this.valueNode.append("<TARGETING>");
        this.valueNode.append("\n");
        for (Entity target: entity.getTargetingList()) {
            this.valueNode.append("<TARGET entityReference=" + "\"" + target.toString() + "\"/>");
        }

        this.valueNode.append("</TARGETING>");
        this.valueNode.append("\n");
        this.valueNode.append("</" + entity.getClass().getSimpleName() + ">");
    }

    private void processSkill(Skill skill, int skillLevel) {
        StringBuffer skillString = new StringBuffer("<" + skill.getClass().getSimpleName()
                + " reference=" + "\"" + skill.toString() + "\""
                + " name=" + "\"" + skill.getName() + "\""
                + " accuracy=" + "\"" + skill.getAccuracy() + "\""
                + " useCost=" + "\"" + skill.getUseCost()  + "\""
                + " level=" + "\"" + skillLevel + "\"" + ">");

        this.valueNode.append(skillString);
        visitInfluenceEffect(skill.getInfluenceEffect());
        skill.getBehavior().accept(this);
//        visitSettableCommand(skill.getBehavior());
        this.valueNode.append("</" + skill.getClass().getSimpleName() + ">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    private void visitItemHotBar(ItemHotBar itemHotBar) {
        StringBuffer hotBarString = new StringBuffer("<" + itemHotBar.getClass().getSimpleName() + ">");
        this.valueNode.append(hotBarString);
        this.valueNode.append("\n");
        this.valueNode.append("<ITEMLIST>");
        this.valueNode.append("\n");

        for(Map.Entry<Integer, TakeableItem> entry: itemHotBar.getItemBarMap().entrySet()) {
            this.valueNode.append("<INTEGERKEY key=" + "\"" + entry.getKey() + "\"" + "/>");
            entry.getValue().accept(this);
            this.valueNode.append("\n");
        }

        this.valueNode.append("</ITEMLIST>");
        this.valueNode.append("\n");
        this.valueNode.append("</" + itemHotBar.getClass().getSimpleName() + ">");
    }

    private void visitSettableCommand(SettableCommand behavior) {
        StringBuffer commandString = new StringBuffer("<" + behavior.getClass().getSimpleName()
                + " amount=" + "\"" + behavior.getAmount() + "\""
                + " reference=" + "\"" + behavior.toString() + "\"" + "/>");
        this.valueNode.append(commandString);
    }

    public void visitEquipment(Equipment equipment) {
        StringBuffer equipString = new StringBuffer("<" + equipment.getClass().getSimpleName() + ">" + "\n" + "\t");
        this.valueNode.append(equipString);

        if(equipment.hasArmor()) {
            visitItem(equipment.getEquippedArmor());
        }

        if(equipment.hasRing()) {
            visitItem(equipment.getEquippedRing());
        }

        if(equipment.hasWeapon()) {
            visitItem(equipment.getEquippedWeapon());
        }

        this.valueNode.append("\n");
        this.valueNode.append("</" + equipment.getClass().getSimpleName() + ">");
    }

    public void visitInventory(Inventory inventory) {
        StringBuffer invString = new StringBuffer("<" + inventory.getClass().getSimpleName()
                + " maxSize=" + "\"" + inventory.getMaxSize() + "\">");
        this.valueNode.append(invString);
        this.valueNode.append("\n");
        this.valueNode.append("<ITEMLIST>");
        inventory.accept(this);
        this.valueNode.append("</ITEMLIST>");
        this.valueNode.append("</" + inventory.getClass().getSimpleName() + ">");
    }

    public void visitInfiniteAreaEffect(InfiniteAreaEffect infiniteAreaEffect) {
        StringBuffer areaEffect = new StringBuffer("<" + infiniteAreaEffect.getClass().getSimpleName() + ">");

        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(areaEffect);
        infiniteAreaEffect.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + infiniteAreaEffect.getClass().getSimpleName() + ">");
    }

    public void visitOneShotAreaEffect(OneShotAreaEffect oneShotAreaEffect) {
        StringBuffer areaEffect = new StringBuffer("<" + oneShotAreaEffect.getClass().getSimpleName() +
                " hasNotFired=" + "\"" + oneShotAreaEffect.hasNotFired() + "\"" + ">");

        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(areaEffect);
        oneShotAreaEffect.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + oneShotAreaEffect.getClass().getSimpleName() + ">");
    }

    public void visitMount(Mount mount) {
        StringBuffer mountString = new StringBuffer("<" + mount.getClass().getSimpleName()
                + " orientation=" + "\"" + mount.getOrientation() + "\""
                + " speed=" + "\"" + mount.speedToString() + "\">");

        mountString.append("\n");
        mountString.append("<TERRAINLIST>");
        for(Terrain terrain: mount.getPassableTerrain()) {
            mountString.append("\t");
            mountString.append("\n");
            mountString.append("<TERRAIN value=\"");
            mountString.append(terrain.toString());
            mountString.append("\"/>");
        }

        mountString.append("\n");
        mountString.append("</TERRAINLIST>");
        this.valueNode.append(mountString);
        this.valueNode.append("\n");
        this.valueNode.append("</" + mount.getClass().getSimpleName() + ">");
    }

    public void visitItem(ConsumableItem item) {
        processTakeableItem(item);
    }

    public void visitItem(WeaponItem item) {
        StringBuffer weaponString = new StringBuffer("<" + item.getClass().getSimpleName()
                + " price=" + "\"" + item.getPrice() + "\""
                + " name=" + "\"" + item.getName() + "\""
                + " damage=" + "\"" + item.getAttackDamage() + "\""
                + " speed=" + "\"" + item.getAttackSpeed()+ "\""
                + " accuracy=" + "\"" + item.getAccuracy() + "\""
                + " useCost=" + "\"" + item.getUseCost() + "\""
                + " range=" + "\"" + item.getRange() + "\""
                + " reference=" + "\"" + item.toString() + "\">");

        this.valueNode.append(weaponString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        item.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        processSkill(item.getHostSKill());
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        visitInfluenceEffect(item.getInfluenceEffect());
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + item.getClass().getSimpleName() + ">");
    }

    private void processSkill(Skill skill) {
        StringBuffer skillString = new StringBuffer("<" + skill.getClass().getSimpleName()
                + " name=" + "\"" + skill.getName() + "\""
                + " accuracy=" + "\"" + skill.getAccuracy() + "\""
                + " useCost=" + "\"" + skill.getUseCost() + "\""
                + " reference=" + "\"" +  skill.toString() + "\">");

        this.valueNode.append(skillString);
        visitInfluenceEffect(skill.getInfluenceEffect());
        visitSettableCommand(skill.getBehavior());
        this.valueNode.append("</" + skill.getClass().getSimpleName() + ">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    public void visitItem(InteractiveItem item) {
        processItem(item);
    }

    public void visitItem(ArmorItem armorItem) {
        StringBuffer itemString = new StringBuffer("<" + armorItem.getClass().getSimpleName()
                + " price=" + "\"" + armorItem.getPrice() + "\""
                + " name=" + "\"" + armorItem.getName() + "\""
                + " isToBeDeleted=" + "\"" + armorItem.isToBeDeleted() + "\""
                + " defense=" + "\"" + armorItem.getDefense() + "\""
                + " reference=" + "\"" + armorItem.toString() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(itemString);
        armorItem.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + armorItem.getClass().getSimpleName() + ">");
    }

    public void visitItem(RingItem item) {
        processTakeableItem(item);
    }

    public void visitItem(OneShotItem item) {
        processItem(item);
    }

    private void processItem(Item item) {
        StringBuffer itemString = new StringBuffer("<" + item.getClass().getSimpleName()
                + " name=" + "\"" + item.getName() + "\""
                + " isToBeDeleted=" + "\"" + item.isToBeDeleted() + "\""
                + " reference=" + "\"" + item.toString() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(itemString);
        item.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + item.getClass().getSimpleName() + ">");
    }

    private void processTakeableItem(TakeableItem item) {
        StringBuffer itemString = new StringBuffer("<" + item.getClass().getSimpleName()
                + " price=" + "\"" + item.getPrice() + "\""
                + " name=" + "\"" + item.getName() + "\""
                + " isToBeDeleted=" + "\"" + item.isToBeDeleted() + "\""
                + " reference=" + "\"" + item.toString() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(itemString);
        item.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + item.getClass().getSimpleName() + ">");
    }

    public void visitTrap(Trap trap) {
        StringBuffer trapString = new StringBuffer("<" + trap.getClass().getSimpleName()
                + " isVisible=" + "\"" + trap.getIsVisible() + "\""
                + " isDisarmed=" + "\"" + trap.getIsDisarmed() + "\""
                + " trapStrength=" + "\"" + trap.getTrapStrength() + "\"" +">");

        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(trapString);
        trap.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + trap.getClass().getSimpleName() + ">");
    }

    public void visitRiver(River river) {
        StringBuffer vectorString = vecToString(river.getFlowrate());
        StringBuffer riverString = new StringBuffer("<" + river.getClass().getSimpleName() +
                " flowRate=" + "\"");

        riverString.append(vectorString);
        riverString.append("\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(riverString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    public void visitTerrain(Terrain terrain) {
        // TODO: implement?
    }

    public void visitDecal(Decal decal) {
        // TODO: implement?
    }

    public void visitConfusedAI(ConfusedAI confusedAI) {
        // TODO: implement?
    }

    public void visitFriendlyAI(FriendlyAI friendlyAI) {
        // TODO: implement?
    }

    public void visitHostileAI(HostileAI hostileAI) {
    }

    public void visitFrozenAI(FrozenAI frozenAI) {
        // TODO: implement?
    }

    public void visitRemoveHealthCommand(RemoveHealthCommand removeHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + removeHealthCommand.getClass().getSimpleName()
                + " amount=" + "\"" + removeHealthCommand.getAmount() + "\""
                + " reference=" + "\"" + removeHealthCommand.toString() + "\"/>");
    }

    public void visitLevelUpCommand(LevelUpCommand levelUpCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + levelUpCommand.getClass().getSimpleName()
                + " reference=" + "\"" + levelUpCommand.toString() + "\"/>");
    }

    public void visitSendInfluenceEffectCommand(SendInfluenceEffectCommand sendInfluenceEffectCommand) {
        // TODO: implement?
    }

    public void visitDropItemCommand(DropItemCommand dropItemCommand) {
        StringBuffer dropString = new StringBuffer("<" + dropItemCommand.getClass().getSimpleName()
                + " reference=" + "\"" + dropItemCommand.toString() + "\">");
        this.valueNode.append(dropString);
        dropItemCommand.getEntity().accept(this);
        dropItemCommand.getItem().accept(this);
        this.valueNode.append("</" + dropItemCommand.getClass().getSimpleName() + ">");
    }

    public void visitTeleportEntityCommand(TeleportEntityCommand teleportEntityCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        StringBuffer point = keyToString(teleportEntityCommand.getDestinationPoint());
        StringBuffer teleportString = new StringBuffer("<" + teleportEntityCommand.getClass().getSimpleName()
                + " levelReference=" +  "\"" + teleportEntityCommand.levelReference() + "\""
                + " point=" +  "\"" + point +  "\""
                + " reference=" +  "\"" + teleportEntityCommand.toString() +  "\"/>");
        this.valueNode.append(teleportString);
    }

    public void visitDialogCommand(DialogCommand dialogCommand) {
        // TODO: implement?
    }

    public void visitInstaDeathCommand(InstaDeathCommand instaDeathCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + instaDeathCommand.getClass().getSimpleName()
                + " reference=" +  "\"" + instaDeathCommand.toString() +  "\"/>");
    }

    public void visitToggleHealthCommand(ToggleHealthCommand toggleHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleHealthCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleHealthCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleHealthCommand.hasFired() + "\""
                + " reference=" + "\"" + toggleHealthCommand.toString() + "\"/>");
    }

    public void visitToggleManaCommand(ToggleManaCommand toggleManaCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleManaCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleManaCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleManaCommand.hasFired() + "\""
                + " reference=" + "\"" + toggleManaCommand.toString() + "\"/>");
    }

    public void visitSpeedCommand(ToggleSpeedCommand toggleSpeedCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleSpeedCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleSpeedCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleSpeedCommand.hasFired() + "\""
                + " reference=" + "\"" + toggleSpeedCommand.toString() + "\"/>");
    }

    public void visitToggleSneaking(ToggleSneaking toggleSneaking) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleSneaking.getClass().getSimpleName()
                + " amount=" + "\"" + toggleSneaking.getAmount() + "\""
                + " hasFired=" + "\"" + toggleSneaking.hasFired() + "\""
                + " oldSpeed=" + "\"" + toggleSneaking.getOldSpeed() + "\""
                + " oldNoise=" + "\"" + toggleSneaking.getOldNoise() + "\""
                + " firstTimeExecuting=" + "\"" + toggleSneaking.hasFirstTimeExecuted() + "\""
                + " reference=" + "\"" + toggleSneaking.toString() + "\"/>");
    }

    public void visitAddHealthCommand(AddHealthCommand addHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + addHealthCommand.getClass().getSimpleName()
                + " amount=" + "\"" + addHealthCommand.getAmount() + "\""
                + " reference=" + "\"" + addHealthCommand.toString() + "\"/>");
    }

    public void visitAddManaCommand(AddManaCommand addManaCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + addManaCommand.getClass().getSimpleName()
                + " amount=" + "\"" + addManaCommand.getAmount() + "\""
                + " reference=" + "\"" + addManaCommand.toString() + "\"/>");
    }

    public void visitBarterCommand(BarterCommand barterCommand) {
        StringBuffer barterString = new StringBuffer("<" + barterCommand.getClass().getSimpleName()
                + " amount=" + "\"" + barterCommand.getAmount() + "\""
                + " reference=" + "\"" + barterCommand.toString() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(barterString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + barterCommand.getClass().getSimpleName() + ">");
    }

    public void visitConfuseEntityCommand(ConfuseEntityCommand confuseEntityCommand) {
        StringBuffer confuseString = new StringBuffer("<" + confuseEntityCommand.getClass().getSimpleName()
                + " amount=" + "\"" + confuseEntityCommand.getAmount() + "\""
                + " reference=" + "\"" + confuseEntityCommand.toString() + "\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(confuseString);
//        confuseEntityCommand.getEntity().accept(this);
//        this.valueNode.append("\n");
//        this.valueNode.append("\t");
//        this.valueNode.append("</" + confuseEntityCommand.getClass().getSimpleName() + "/>");
    }

    public void visitDisarmTrapCommand(DisarmTrapCommand disarmTrapCommand) {
        StringBuffer disarmString = new StringBuffer("<" + disarmTrapCommand.getClass().getSimpleName()
                + " amount=" + "\"" + disarmTrapCommand.getAmount() + "\""
                + " reference=" + "\"" + disarmTrapCommand.toString() + "\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(disarmString);
//        disarmTrapCommand.getEntity().accept(this);
//        this.valueNode.append("\n");
//        this.valueNode.append("\t");
//        this.valueNode.append("</" + disarmTrapCommand.getClass().getSimpleName() + "/>");
    }

    public void visitFreezeEntityCommand(FreezeEntityCommand freezeEntityCommand) {
        StringBuffer freezeString = new StringBuffer("<" + freezeEntityCommand.getClass().getSimpleName()
                + " amount=" + "\"" + freezeEntityCommand.getAmount() + "\""
                + " reference=" + "\"" + freezeEntityCommand.toString() + "\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(freezeString);
//        freezeEntityCommand.getEntity().accept(this);
//        this.valueNode.append("\n");
//        this.valueNode.append("\t");
//        this.valueNode.append("</" + freezeEntityCommand.getClass().getSimpleName() + "/>");
    }

    public void visitObserveEntityCommand(ObserveEntityCommand observeEntityCommand) {
        StringBuffer observeString = new StringBuffer("<" + observeEntityCommand.getClass().getSimpleName()
                + " amount=" + "\"" + observeEntityCommand.getAmount() + "\""
                + " reference=" + "\"" + observeEntityCommand.toString() + "\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(observeString);
    }

    public void visitPickPocketCommand(PickPocketCommand pickPocketCommand) {
        StringBuffer disarmString = new StringBuffer("<" + pickPocketCommand.getClass().getSimpleName()
                + " amount=" + "\"" + pickPocketCommand.getAmount() + "\""
                + " reference=" + "\"" + pickPocketCommand.toString() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(disarmString);
        this.valueNode.append("</" + pickPocketCommand.getClass().getSimpleName() + ">");
    }

    public void visitSlowEntityCommand(SlowEntityCommand slowEntityCommand) {
        StringBuffer slowString = new StringBuffer("<" + slowEntityCommand.getClass().getSimpleName()
                + " amount=" + "\"" + slowEntityCommand.getAmount() + "\""
                + " reference=" + "\"" + slowEntityCommand.toString() + "\"/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(slowString);
//        slowEntityCommand.getEntity().accept(this);
//        this.valueNode.append("\n");
//        this.valueNode.append("\t");
//        this.valueNode.append("</" + slowEntityCommand.getClass().getSimpleName() + "/>");
    }

    public void visitInfluenceEffect(InfluenceEffect influenceEffect) {
        StringBuffer point= keyToString(influenceEffect.getOriginPoint());
        StringBuffer areaEffect = new StringBuffer("<" + influenceEffect.getClass().getSimpleName()
                +" point=" + "\"" + point + "\""
                +" speed=" + "\"" + influenceEffect.getSpeed() + "\""
                +" range=" + "\"" + influenceEffect.getRange() + "\""
                +" movesRemaining=" + "\"" + influenceEffect.getMovesRemaining() + "\""
                +" orientation=" + "\"" + influenceEffect.getOrientation() + "\""
                +" reference=" + "\"" + influenceEffect.toString() + "\">");

        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(areaEffect);
        influenceEffect.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + influenceEffect.getClass().getSimpleName() + ">");
    }

    public void visitObstacle(Obstacle obstacle) {
        StringBuffer obstacleString = new StringBuffer("<" + obstacle.getClass().getSimpleName() + "/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(obstacleString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    @Override
    public void visitSlowedAI(SlowedAI slowedAI) {

    }

    @Override
    public void visitPassivePetState(PassivePetState passivePetState) {

    }

    @Override
    public void visitItemPetState(ItemPetState itemPetState) {

    }

    @Override
    public void visitGeneralPetState(GeneralPetState generalPetState) {

    }

    @Override
    public void visitCombatPetState(CombatPetState combatPetState) {

    }

    @Override
    public void visitPatrolPath(PatrolPath patrolPath) {
        StringBuffer patrol = new StringBuffer("<PATROLPATH>");
        this.valueNode.append(patrol);
        this.valueNode.append("\n");
        for(Vec3d vec: patrolPath.getVectors()) {
            StringBuffer vector = new StringBuffer("<VECTOR vec=" + "\"" + vecToString(vec) + "\"/>");
            this.valueNode.append(vector);
        }
        this.valueNode.append("</PATROLPATH>");
    }

    public void saveCurrentLevel(Level currentLevel) throws IOException {
        visitLevel(currentLevel);
        this.currentLevel.append("\n");
        this.currentLevel.append(levelString);
        this.currentLevel.append("\n");
        this.currentLevel.append("</CURRENTLEVEL>");
        this.currentLevel.append("\n");
        levelString = new StringBuffer("<LEVEL");
        writer.write(this.currentLevel.toString());
    }

    public void saveLevelList(List<Level> levels) throws IOException {
        this.levelList.append("\n");
        for(Level curLevel: levels) {
            visitLevel(curLevel);
            this.levelList.append(levelString);
            levelString = new StringBuffer("<LEVEL");
        }

        this.levelList.append("\n");
        this.levelList.append("</LEVELLIST>");
        this.levelList.append("\n");
        writer.write(this.levelList.toString());
    }

    public void visitPlayerEntity(Entity player) throws IOException {
        this.valueNode = new StringBuffer("<PLAYER>");
        visitEntity(player);
        this.valueNode.append("</PLAYER>");
        this.valueNode.append("\n");
        writer.write(this.valueNode.toString());
    }
}
