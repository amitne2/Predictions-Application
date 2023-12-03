package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import entity.Type;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;

public class DecreaseAction extends Action {
    private String entityName;
    private final String propertyName;
    private String by;

    public DecreaseAction(String actionType, String entityName, String properyName, String byExp) throws Exception {
        super(actionType);
        this.entityName = entityName;
        this.propertyName = properyName;
        this.by = byExp;
    }

    public DecreaseAction(String actionType, String entityName, String properyName, String byExp, SeconderyEntity seconderyEntity) throws Exception {
        super(actionType, seconderyEntity);
        this.entityName = entityName;
        this.propertyName = properyName;
        this.by = byExp;
    }

    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance;
        if(context.getPrimaryEntityInstance().getName().equals(entityName))
            entityInstance = context.getPrimaryEntityInstance();
        else
            entityInstance = context.getSecondaryEntityInstance();


        Object decreaseValue = getExpressionValue(context, by, propertyName, entityInstance);
        EntityProperty entityProperty = entityInstance.getProperiesMap().get(propertyName);

        if(entityProperty.getType() == Type.DECIMAL)
            decreaseInt(entityProperty, decreaseValue, context.getCurrTick());
         else if (entityProperty.getType() == Type.FLOAT) //float type
            decreaseFloat(entityProperty, decreaseValue, context.getCurrTick());
         else
            throw new Exception("can not decrease property from type: " + entityProperty.getType());

        entityInstance.getProperiesMap().replace(propertyName, entityProperty); //replace with the new value
    }

    @Override
    protected Object getValue(Map <String, EntityProperty> propertyMap, String by) {
        EntityProperty entityProperty = propertyMap.get(propertyName);
        Type type = entityProperty.getType();
        if(type == Type.FLOAT) {
            return Float.parseFloat(by);
        }
        else { //decimal
            return Integer.parseInt(by);
        }
    }

    @Override
    public String getActionName() {
        return "decrease";
    }

    public void decreaseInt(EntityProperty entityProperty, Object value, long currTick) {
        int res = (int) entityProperty.getValue() - (int)value;

        if(entityProperty.isExistRange()) { //check res locate in range
            if(res >= entityProperty.getFromRange() && res <= entityProperty.getToRange()) {

                if(res != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                    entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
                } else {
                    entityProperty.setAnotherTickAsTheSameValue(currTick);
                }
                entityProperty.setValue(res);
            } else {
                entityProperty.setAnotherTickAsTheSameValue(currTick);
            }
        } else { //without  range
            if(res != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
            } else {
                entityProperty.setAnotherTickAsTheSameValue(currTick);
            }
            entityProperty.setValue(res); //no range for this property. set res value
        }
    }

    public void decreaseFloat(EntityProperty entityProperty, Object value, long currTick) {
        float res = (float) entityProperty.getValue() - (float)value;

        if(entityProperty.isExistRange()) { //check res locate in range
            if(res >= entityProperty.getFromRange() && res <= entityProperty.getToRange()) {

                if(res != (float) entityProperty.getValue()) {
                    //set in changesList of property, the tick the property changed
                    entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
                } else { //set the end tick in last element in list
                    entityProperty.setAnotherTickAsTheSameValue(currTick);
                }
                entityProperty.setValue(res);
            }
            else {
                entityProperty.setAnotherTickAsTheSameValue(currTick);
            }
        }
        else { //range doesn't exist
            if(res != (float) entityProperty.getValue()) {
                //set in changesList of property, the tick the property changed
                entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
            } else {
                entityProperty.setAnotherTickAsTheSameValue(currTick);
            }
            entityProperty.setValue(res); //no range for this property. set res value
        }
    }

    public String getBy() {
        return by;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEntityName() {
        return entityName;
    }
}
