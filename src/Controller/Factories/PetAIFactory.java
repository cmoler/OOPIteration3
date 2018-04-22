package Controller.Factories;

import Model.AI.AIState;
import Model.AI.PathingAlgorithm;
import Model.AI.PetAI.PetStates.CombatPetState;
import Model.AI.PetAI.PetStates.GeneralPetState;
import Model.AI.PetAI.PetStates.ItemPetState;
import Model.AI.PetAI.PetStates.PassivePetState;
import Model.Entity.Entity;
import Model.Entity.EntityAttributes.Skill;
import Model.Item.Item;
import Model.Level.Obstacle;
import Model.Level.River;
import Model.Level.Terrain;
import Model.Utility.BidiMap;
import javafx.geometry.Point3D;

import java.util.List;
import java.util.Map;

public class PetAIFactory {

    private Map<Point3D, Terrain> terrainMap;
    private Map<Point3D, Obstacle> obstacleMap;
    private Map<Point3D, Item> itemMap;
    private Skill pickpock;
    private Map<Point3D, River> riverMap;
    private BidiMap<Point3D, Entity> entityMap;
    private Entity player;
    private Entity pet;

    private CombatPetState combatPetState;
    private GeneralPetState generalPetState;
    private ItemPetState itemPetState;
    private PassivePetState passivePetState;

    public PetAIFactory(Map<Point3D, Terrain> terrainMap, Map<Point3D, Obstacle> obstacleMap, Map<Point3D, Item> itemMap,
                        Skill pickpock, Map<Point3D, River> riverMap, BidiMap<Point3D, Entity> entityMap, Entity player, Entity pet){

        this.terrainMap = terrainMap;
        this.obstacleMap = obstacleMap;
        this.itemMap = itemMap;
        this.pickpock = pickpock;
        this.riverMap = riverMap;
        this.entityMap = entityMap;
        this.player = player;
        this.pet = pet;
    }

    public CombatPetState getCombatPetState(){
        if(combatPetState == null){
            combatPetState = new CombatPetState(pet, terrainMap, entityMap, obstacleMap, player, riverMap);
        }
        return combatPetState;
    }

    public GeneralPetState getGeneralPetState(){
        if(generalPetState == null){
            generalPetState = new GeneralPetState(pet, terrainMap, entityMap, obstacleMap, itemMap, pickpock, player, riverMap);
        }
        return generalPetState;
    }

    public ItemPetState getItemPetState(){
        if(itemPetState == null){
            itemPetState = new ItemPetState(pet, terrainMap, entityMap, obstacleMap, itemMap, player, pickpock, riverMap);
        }
        return itemPetState;
    }

    public PassivePetState getPassivePetState(){
        if(passivePetState == null){
            passivePetState = new PassivePetState(pet, terrainMap, entityMap, obstacleMap, player, riverMap);
        }
        return passivePetState;
    }
}
