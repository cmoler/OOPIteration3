package Controller.Visitor;

import Model.AI.ConfusedAI;
import Model.AI.FriendlyAI;
import Model.AI.FrozenAI;
import Model.AI.HostileAI;
import Model.AreaEffect.AreaEffect;
import Model.AreaEffect.InfiniteAreaEffect;
import Model.AreaEffect.OneShotAreaEffect;
import Model.Command.Command;
import Model.Command.EntityCommand.NonSettableCommand.*;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleHealthCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleManaCommand;
import Model.Command.EntityCommand.NonSettableCommand.ToggleableCommand.ToggleSpeedCommand;
import Model.Command.EntityCommand.SettableCommand.*;
import Model.Command.EntityCommand.SettableCommand.ToggleableCommand.ToggleSneaking;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Equipment;
import Model.Entity.EntityAttributes.Inventory;
import Model.Entity.EntityAttributes.Speed;
import Model.InfluenceEffect.AngularInfluenceEffect;
import Model.InfluenceEffect.InfluenceEffect;
import Model.InfluenceEffect.LinearInfluenceEffect;
import Model.InfluenceEffect.RadialInfluenceEffect;
import Model.Item.InteractiveItem;
import Model.Item.Item;
import Model.Item.OneShotItem;
import Model.Item.TakeableItem.ArmorItem;
import Model.Item.TakeableItem.ConsumableItem;
import Model.Item.TakeableItem.RingItem;
import Model.Item.TakeableItem.WeaponItem;
import Model.Level.*;
import com.sun.javafx.geom.Vec3d;
import javafx.geometry.Point3D;
import org.w3c.dom.Document;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SavingVisitor implements Visitor {

    private BufferedWriter writer;
    private StringBuffer valueNode = new StringBuffer("<VALUE>");
    private StringBuffer currentLevel = new StringBuffer("<CURRENTLEVEL>");
    private StringBuffer levelString = new StringBuffer("<LEVEL>");
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

    @Override
    public void visitGameModel(GameModel gameModel) {
        try {
            writer.write(gameModelString.toString());
            gameModel.accept(this);
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
        Map<Point3D, Entity> entityLocations = level.getEntityLocations();
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

            levelString.append("</LEVEL>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void visitEntity(Entity entity) {

    }

    @Override
    public void visitEquipment(Equipment equipment) {

    }

    @Override
    public void visitInventory(Inventory inventory) {

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
    public void visitItem(Item item) {
        StringBuffer itemString = new StringBuffer("<" + item.getClass().getSimpleName()
                + " name=" + "\"" + item.getName() + "\""
                + " isToBeDeleted=" + "\"" + item.isToBeDeleted() + "\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(itemString);
        item.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + item.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitWeaponItem(WeaponItem weaponItem) {

    }

    @Override
    public void visitArmorItem(ArmorItem armorItem) {
        StringBuffer itemString = new StringBuffer("<" + armorItem.getClass().getSimpleName()
                + " name=" + "\"" + armorItem.getName() + "\""
                + " isToBeDeleted=" + "\"" + armorItem.isToBeDeleted() + "\""
                + " defense=" + "\"" + armorItem.getDefense() +"\">");
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append(itemString);
        armorItem.getCommand().accept(this);
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("</" + armorItem.getClass().getSimpleName() + ">");
    }

    @Override
    public void visitRingItem(RingItem ringItem) {

    }

    @Override
    public void visitConsumableItem(ConsumableItem consumableItem) {

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
        this.valueNode.append("<" + removeHealthCommand.getClass().getSimpleName() + " damageAmount=" + "\"" + removeHealthCommand.getAmount() + "\"" + "/>");
    }

    @Override
    public void visitLevelUpCommand(LevelUpCommand levelUpCommand) {

    }

    @Override
    public void visitSendInfluenceEffectCommand(SendInfluenceEffectCommand sendInfluenceEffectCommand) {

    }

    @Override
    public void visitDropItemCommand(DropItemCommand dropItemCommand) {

    }

    @Override
    public void visitTeleportEntityCommand(TeleportEntityCommand teleportEntityCommand) {

    }

    @Override
    public void visitDialogCommand(DialogCommand dialogCommand) {

    }

    @Override
    public void visitInstaDeathCommand(InstaDeathCommand instaDeathCommand) {

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

    }

    @Override
    public void visitSpeedCommand(ToggleSpeedCommand toggleSpeedCommand) {

    }

    @Override
    public void visitToggleSneaking(ToggleSneaking toggleSneaking) {

    }

    @Override
    public void visitAddHealthCommand(AddHealthCommand addHealthCommand) {
        this.valueNode.append("\n");
        this.valueNode.append("\t");
        this.valueNode.append("\t");
        this.valueNode.append("<" + addHealthCommand.getClass().getSimpleName() + " healAmount=" +
                "\"" +addHealthCommand.getAmount() + "\"" + "/>");
    }

    @Override
    public void visitBarterCommand(BarterCommand barterCommand) {

    }

    @Override
    public void visitConfuseEntityCommand(ConfuseEntityCommand confuseEntityCommand) {

    }

    @Override
    public void visitDisarmTrapCommand(DisarmTrapCommand disarmTrapCommand) {

    }

    @Override
    public void visitFreezeEntityCommand(FreezeEntityCommand freezeEntityCommand) {

    }

    @Override
    public void visitObserveEntityCommand(ObserveEntityCommand observeEntityCommand) {

    }

    @Override
    public void visitPickPocketCommand(PickPocketCommand pickPocketCommand) {

    }

    @Override
    public void visitSlowEntityCommand(SlowEntityCommand slowEntityCommand) {

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
}
