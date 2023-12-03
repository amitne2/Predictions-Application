package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import entity.Type;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;

public class IncreaseAction extends Action{
    private final String propertyName;
    private String entityName;
    String by;

   public IncreaseAction(String actionType, String entityName, String propertyName, String byExp) throws Exception {
       super(actionType);
       this.entityName = entityName;
       this.propertyName = propertyName;
       this.by = byExp;
   }

    public IncreaseAction(String actionType, String entityName, String propertyName, String byExp, SeconderyEntity seconderyEntity) throws Exception {
        super(actionType, seconderyEntity);
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.by = byExp;
    }

   //This function increase the value of property name with the value exist in by expression
   @Override
    public void doAction(Context context) throws Exception {
       EntityInstanceImpl entityInstance;
       if(context.getPrimaryEntityInstance().getName().equals(entityName))
            entityInstance = context.getPrimaryEntityInstance();
       else if(context.getSecondaryEntityInstance().getName().equals(entityName))
           entityInstance = context.getSecondaryEntityInstance();
       else
           throw new Exception("The action " + getActionName() + " works on entity: " + entityName + " but the entity doesn't exist on this instance.");


       Object increaseValue = getExpressionValue(context, by, propertyName, entityInstance);
       EntityProperty entityProperty = entityInstance.getProperiesMap().get(propertyName);

       if(entityProperty.getType() == Type.DECIMAL) {
           increaseInt(entityProperty, increaseValue, context.getCurrTick());
       } else if(entityProperty.getType() == Type.FLOAT)//float type
           increaseFloat(entityProperty, increaseValue, context.getCurrTick());
        else
           throw new Exception("can not increase property from type: " + entityProperty.getType());

       entityInstance.getProperiesMap().replace(propertyName, entityProperty); //replace with the new value
   }



    public Object getValue(Map <String, EntityProperty> propertyMap, String by) throws Exception {
       EntityProperty entityProperty = propertyMap.get(propertyName);
       Type type = entityProperty.getType();
       if(type == Type.FLOAT) {
           return Float.parseFloat(by);
       }
       else if(type == Type.DECIMAL ){ //decimal
           return Integer.parseInt(by);
       } else {
           throw new Exception("In increase action can get only decimal/float value");
       }
   }
    @Override
    public String getActionName() {
        return "increase";
    }

    public void increaseInt(EntityProperty entityProperty, Object value, long currTick) throws Exception {
       try {
           int res = (int) entityProperty.getValue() + (int)value;
           if(entityProperty.isExistRange()) { //check res locate in range
               if(res >= entityProperty.getFromRange() && res <= entityProperty.getToRange()) {

                   if(res != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                       entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
                   }  else {
                       entityProperty.setAnotherTickAsTheSameValue(currTick);
                   }
                   entityProperty.setValue(res);
               } else {
                   entityProperty.setAnotherTickAsTheSameValue(currTick);
               }

           } else { //without range
               if(res != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                   entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
               }  else {
                   entityProperty.setAnotherTickAsTheSameValue(currTick);
               }
               entityProperty.setValue(res); //no range for this property. set res value
           }
       }catch (Exception e) {
           throw new Exception("Can not convert " + value + " to decimal type for property: " + entityProperty.getName());
       }
    }

    public void increaseFloat(EntityProperty entityProperty, Object value, long currTick) throws Exception {
       try {
           float res = (float) entityProperty.getValue() + (float) value;
           if(entityProperty.isExistRange()) { //check res locate in range
               if(res >= entityProperty.getFromRange() && res <= entityProperty.getToRange()) {

                   if(res != (float) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                       entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
                   } else {
                       entityProperty.setAnotherTickAsTheSameValue(currTick);
                   }
                   entityProperty.setValue(res);
               } else { //res not in range, stay with the value as before
                   entityProperty.setAnotherTickAsTheSameValue(currTick);
               }
               /*else
                   entityProperty.setValue((float) entityProperty.getValue()); //res not in range. stay with value as before*/
           }
           else {
               if(res != (float) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                   entityProperty.setEndTickAndAddNewElementForNextOne(currTick, res);
               }  else {
                   entityProperty.setAnotherTickAsTheSameValue(currTick);
               }

               entityProperty.setValue(res); //no range for this property. set res value
           }
       } catch (Exception e) {
           throw new Exception("Can not convert " + value + " to float type for property: " + entityProperty.getName());
       }
   }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getBy() {
       return by;
    }
}
