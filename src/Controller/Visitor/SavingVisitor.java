package Controller.Visitor;

import Model.AI.ConfusedAI;
import Model.AI.FriendlyAI;
import Model.AI.FrozenAI;
import Model.AI.HostileAI;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SavingVisitor implements Visitor {

    private BufferedWriter writer;
    private StringBuffer valueNode = new StringBuffer("<VALUE>");
    private StringBuffer currentLevel = new StringBuffer("<CURRENTLEVEL>");
    private StringBuffer levelString = new StringBuffer("<LEVEL>");
    private StringBuffer levelList = new StringBuffer("<LEVELLIST>");
    private StringBuffer gameModelString = new StringBuffer("<GAMEMODEL>\n");
    private StringBuffer entityNode = new StringBuffer("<ENTITY>");
    private ArrayList<String> refStrings = new ArrayList<>();

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

    private StringBuffer processitems(Map<Point3D, Item> itemLocations) {
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

    @Override
    public void visitGameModel(GameModel gameModel) {
        try {
            writer.write(gameModelString.toString());

            try {
                if(gameModel.hasCurrentLevel()) {
                    saveCurrentLevel(gameModel.getCurrentLevel());
                    System.out.println("here1");
                }

                if(gameModel.hasLevels()) {
                    saveLevelList(gameModel.getLevels());
                    System.out.println("here2");
                }

                if(gameModel.hasPlayer()) {
                    visitPlayerEntity(gameModel.getPlayer());
                    System.out.println("here3");
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

    @Override
    public void visitLevel(Level level) {
        Map<Point3D, Terrain> terrainLocations = level.getTerrainLocations();
        Map<Point3D, Item> itemLocations = level.getItemLocations();
        Map<Point3D, Obstacle> obstacleLocations = level.getObstacleLocations();
        BidiMap<Point3D, Entity> entityLocations = level.getEntityLocations();
        Map<Point3D, AreaEffect> areaEffectLocations = level.getAreaEffectLocations();
        Map<Point3D, Trap> trapLocations = level.getTrapLocations();
        Map<Point3D, River> riverLocations = level.getRiverLocations();
        Map<Point3D, Mount> mountLocations = level.getMountLocations();
        Map<Point3D, InfluenceEffect> influenceEffectLocations = level.getInfluenceEffectLocations();
        Map<Point3D, Decal> decalLocations = level.getDecalLocations();

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
            levelString.append(processitems(itemLocations));
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

    @Override
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
                + " speed=" + "\"" + entity.getSpeed() + "\""
                + " goldAmount=" + "\"" + entity.getGold() + "\""
                + " maxGold=" + "\"" + entity.getMaxGold() + "\""
                + " attackPoints=" + "\"" + entity.getAttackPoints() + "\""
                + " attackModifier=" + "\"" + entity.getAttackModifier() + "\""
                + " defensePoints=" + "\"" + entity.getDefensePoints() + "\""
                + " defenseModifier=" + "\"" + entity.getDefenseModifier() + "\""
                + " orientation=" + "\"" + entity.getOrientation() + "\""
                + " moveable=" + "\"" + entity.isMoveable() + "\""
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
        visitMount(entity.getMount());

        this.valueNode.append("\n");
        visitEquipment(entity.getEquipment());
        this.valueNode.append("\n");

        visitInventory(entity.getInventory());
        this.valueNode.append("\n");

        visitItemHotBar(entity.getItemHotBar());
        this.valueNode.append("\n");
        this.valueNode.append("</" + entity.getClass().getSimpleName() + ">");
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

    private void processSkill(Skill weaponSkill, int skillLevel) {
        StringBuffer skillString = new StringBuffer("<" + weaponSkill.getClass().getSimpleName()
                + " name=" + "\"" + weaponSkill.getName() + "\""
                + " accuracy=" + "\"" + weaponSkill.getAccuracy() + "\""
                + " useCost=" + "\"" + weaponSkill.getUseCost()  + "\""
                + " level=" + "\"" + skillLevel + "\"" + ">");

        this.valueNode.append(skillString);
        visitInfluenceEffect(weaponSkill.getInfluenceEffect());
        visitSettableCommand(weaponSkill.getBehavior());
        visitSendInfluenceEffectCommand(weaponSkill.getSendInfluenceEffectCommand());
        this.valueNode.append("</" + weaponSkill.getClass().getSimpleName() + ">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    private void visitSettableCommand(SettableCommand behavior) {
        StringBuffer commandString = new StringBuffer("<" + behavior.getClass().getSimpleName()
                + " amount=" + "\"" + behavior.getAmount() + "\"/>");
        this.valueNode.append(commandString);
    }

    @Override
    public void visitEquipment(Equipment equipment) {
        StringBuffer equipString = new StringBuffer("<" + equipment.getClass().getSimpleName() + ">");
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void visitItem(ConsumableItem item) {
        processItem(item);
    }

    @Override
    public void visitItem(WeaponItem item) {
        StringBuffer weaponString = new StringBuffer("<" + item.getClass().getSimpleName()
                + " price=" + "\"" + item.getPrice() + "\""
                + " name=" + "\"" + item.getName() + "\""
                + " attackDamage=" + "\"" + item.getAttackDamage() + "\""
                + " attackSpeed=" + "\"" + item.getAttackSpeed()+ "\""
                + " accuracy=" + "\"" + item.getAccuracy() + "\""
                + " useCost=" + "\"" + item.getUseCost() + "\""
                + " range=" + "\"" + item.getRange() + "\">");

        this.valueNode.append(weaponString);
        this.valueNode.append("</" + item.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitItem(InteractiveItem item) {
        processItem(item);
    }

    @Override
    public void visitItem(ArmorItem armorItem) {
        StringBuffer itemString = new StringBuffer("<" + armorItem.getClass().getSimpleName()
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

    @Override
    public void visitItem(RingItem item) {
        processItem(item);
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void visitTerrain(Terrain terrain) {

    }

    @Override
    public void visitDecal(Decal decal) {

    }

    @Override
    public void visitConfusedAI(ConfusedAI confusedAI) {

    }

    @Override
    public void visitFriendlyAI(FriendlyAI friendlyAI) {

    }

    @Override
    public void visitHostileAI(HostileAI hostileAI) {

    }

    @Override
    public void visitFrozenAI(FrozenAI frozenAI) {

    }

    @Override
    public void visitRemoveHealthCommand(RemoveHealthCommand removeHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + removeHealthCommand.getClass().getSimpleName() + " amount=" + "\"" + removeHealthCommand.getAmount() + "\"" + "/>");
    }

    @Override
    public void visitLevelUpCommand(LevelUpCommand levelUpCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + levelUpCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitSendInfluenceEffectCommand(SendInfluenceEffectCommand sendInfluenceEffectCommand) {

    }

    @Override
    public void visitDropItemCommand(DropItemCommand dropItemCommand) {
        StringBuffer dropString = new StringBuffer("<" + dropItemCommand.getClass().getSimpleName() + ">");
        this.valueNode.append(dropString);
        dropItemCommand.getEntity().accept(this);
        dropItemCommand.getItem().accept(this);
        this.valueNode.append("</" + dropItemCommand.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitTeleportEntityCommand(TeleportEntityCommand teleportEntityCommand) {
        StringBuffer point = keyToString(teleportEntityCommand.getDestinationPoint());
        StringBuffer teleportString = new StringBuffer("<" + teleportEntityCommand.getClass().getSimpleName()
                + "levelReference=" +  "\"" + teleportEntityCommand.levelReference() + "\""
                + "point=" +  "\"" + point +  "\"" + ">");
        this.valueNode.append(teleportString);
        this.valueNode.append("</" + teleportEntityCommand.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitDialogCommand(DialogCommand dialogCommand) {

    }

    @Override
    public void visitInstaDeathCommand(InstaDeathCommand instaDeathCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + instaDeathCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitToggleHealthCommand(ToggleHealthCommand toggleHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleHealthCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleHealthCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleHealthCommand.hasFired() + "\"" + "/>");
    }

    @Override
    public void visitToggleManaCommand(ToggleManaCommand toggleManaCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleManaCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleManaCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleManaCommand.hasFired() + "\"" + "/>");
    }

    @Override
    public void visitSpeedCommand(ToggleSpeedCommand toggleSpeedCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleSpeedCommand.getClass().getSimpleName()
                + " amount=" + "\"" + toggleSpeedCommand.getAmount() + "\""
                + " hasFired=" + "\"" + toggleSpeedCommand.hasFired() + "\"" + "/>");
    }

    @Override
    public void visitToggleSneaking(ToggleSneaking toggleSneaking) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + toggleSneaking.getClass().getSimpleName()
                + " amount=" + "\"" + toggleSneaking.getAmount() + "\""
                + " hasFired=" + "\"" + toggleSneaking.hasFired() + "\"" + "/>");
    }

    @Override
    public void visitAddHealthCommand(AddHealthCommand addHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + addHealthCommand.getClass().getSimpleName() + " amount=" +
                "\"" +addHealthCommand.getAmount() + "\"" + "/>");
    }

    @Override
    public void visitBarterCommand(BarterCommand barterCommand) {
        StringBuffer barterString = new StringBuffer("<" + barterCommand.getClass().getSimpleName()
                + " barterStrength=" + "\"" + barterCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(barterString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + barterCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitConfuseEntityCommand(ConfuseEntityCommand confuseEntityCommand) {
        StringBuffer confuseString = new StringBuffer("<" + confuseEntityCommand.getClass().getSimpleName()
                + " confusionDuration=" + "\"" + confuseEntityCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(confuseString);
        confuseEntityCommand.getEntity().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + confuseEntityCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitDisarmTrapCommand(DisarmTrapCommand disarmTrapCommand) {
        StringBuffer disarmString = new StringBuffer("<" + disarmTrapCommand.getClass().getSimpleName()
                + " disarmStrength=" + "\"" + disarmTrapCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(disarmString);
        disarmTrapCommand.getEntity().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + disarmTrapCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitFreezeEntityCommand(FreezeEntityCommand freezeEntityCommand) {
        StringBuffer freezeString = new StringBuffer("<" + freezeEntityCommand.getClass().getSimpleName()
                + " freezeDuration=" + "\"" + freezeEntityCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(freezeString);
        freezeEntityCommand.getEntity().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + freezeEntityCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitObserveEntityCommand(ObserveEntityCommand observeEntityCommand) {

    }

    @Override
    public void visitPickPocketCommand(PickPocketCommand pickPocketCommand) {
        StringBuffer disarmString = new StringBuffer("<" + pickPocketCommand.getClass().getSimpleName()
                + " pickpocketStrength=" + "\"" + pickPocketCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(disarmString);
        this.valueNode.append("</" + pickPocketCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitSlowEntityCommand(SlowEntityCommand slowEntityCommand) {
        StringBuffer slowString = new StringBuffer("<" + slowEntityCommand.getClass().getSimpleName()
                + " slowDuration=" + "\"" + slowEntityCommand.getAmount() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(slowString);
        slowEntityCommand.getEntity().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + slowEntityCommand.getClass().getSimpleName() + "/>");
    }

    @Override
    public void visitInfluenceEffect(InfluenceEffect influenceEffect) {
        StringBuffer areaEffect = new StringBuffer("<" + influenceEffect.getClass().getSimpleName()
                +" speed=" + "\"" + influenceEffect.getSpeed() + "\""
                +" range=" + "\"" + influenceEffect.getRange() + "\""
                +" movesRemaining=" + "\"" + influenceEffect.getMovesRemaining() + "\""
                +" orientation=" + "\"" + influenceEffect.getOrientation() + "\"" +">");

        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(areaEffect);
        influenceEffect.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + influenceEffect.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitObstacle(Obstacle obstacle) {
        StringBuffer obstacleString = new StringBuffer("<" + obstacle.getClass().getSimpleName() + "/>");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(obstacleString);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
    }

    public void saveCurrentLevel(Level currentLevel) throws IOException {
        visitLevel(currentLevel);
        this.currentLevel.append("\n");
        this.currentLevel.append(levelString);
        this.currentLevel.append("\n");
        this.currentLevel.append("</CURRENTLEVEL>");
        this.currentLevel.append("\n");
        levelString = new StringBuffer("<LEVEL>");
        writer.write(this.currentLevel.toString());
    }

    public void saveLevelList(List<Level> levels) throws IOException {
        this.levelList.append("\n");
        for(Level curLevel: levels) {
            visitLevel(curLevel);
            this.levelList.append(levelString);
            levelString = new StringBuffer("<LEVEL>");
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
