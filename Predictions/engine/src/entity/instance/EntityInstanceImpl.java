package entity.instance;

import entity.Type;
import grid.Position;
import property.EntityDefProperty;
import property.EntityProperty;
import rule.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityInstanceImpl implements EntityInstance{

   private String name;
   private int id;
    private Map<String, EntityProperty> entityProperiesDetails;
    private boolean needToBeKilled;
    private Position positionOnGrid;


    public EntityInstanceImpl(String name, List <EntityDefProperty> entityDefProperties, int id, int row, int col) {
        this.id = id;
        entityProperiesDetails = new HashMap<>();
        this.name = name;
        this.needToBeKilled = false;
        this.positionOnGrid = new Position(row, col);

        for(EntityDefProperty singleProperty: entityDefProperties) {
            Object value = singleProperty.getValueProperty(); //get property value
            EntityProperty propertyToAdd = new EntityProperty(singleProperty, value);
            entityProperiesDetails.put(singleProperty.getName(), propertyToAdd); //add to entity property list
        }
    }
    @Override
    public EntityProperty getDetailOfProperty(String name) throws Exception {
        try {
            return entityProperiesDetails.get(name);
        } catch (Exception e) {
            throw new Exception("No such property name in this instance");
        }

    }
    /*public void performActionsByRule(EnvVariableManagerImpl envVariableManager, Rule rule) throws Exception {

        for(Action currentAction: rule.getActionsToPerform()) {
            currentAction.doAction(envVariableManager,this);
        }
    }*/
    @Override
    public Map<String, EntityProperty> getProperiesMap() {
        return entityProperiesDetails;
    }

    @Override
    public void addPropertyInstance(EntityProperty entityProperty) {
        entityProperiesDetails.put(entityProperty.getName(), entityProperty);
    }
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() { return name; }

    public void KillInstance() {
        this.needToBeKilled = true;
    }

    public boolean isNeedToBeKilled(){ return needToBeKilled; }

    public Position getPositionOnGrid(){
        return positionOnGrid;
    }

    public void setLocationInGrid(Position randomLocation) {
        this.positionOnGrid.setLocation(randomLocation);
    }

    public EntityProperty getPropertyByName(String name) {
        return entityProperiesDetails.get(name);
    }

    public Type getTypeOfPropertyByName(String name) {
        return entityProperiesDetails.get(name).getType();
    }

}
