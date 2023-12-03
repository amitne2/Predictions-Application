package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import entity.Type;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;

public class CalculationAction extends Action {
    private String entityName;

    private String calculationType;
    private String resultProp;
    private String arg1;
    private String arg2;

    public CalculationAction(String entityName, String actionType, String calculationType,
                             String resultProp, String arg1, String arg2) throws Exception {
        super(actionType);
        this.entityName=entityName;
        this.calculationType = calculationType;
        this.resultProp = resultProp;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    public CalculationAction(String entityName, String actionType, String calculationType,
                             String resultProp, String arg1, String arg2, SeconderyEntity seconderyEntity) throws Exception {
        super(actionType, seconderyEntity);
        this.entityName = entityName;
        this.calculationType = calculationType;
        this.resultProp = resultProp;
        this.arg1 = arg1;
        this.arg2 = arg2;
    }

    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance;
        if(context.getPrimaryEntityInstance().getName().equals(entityName))
            entityInstance = context.getPrimaryEntityInstance();
        else
            entityInstance = context.getSecondaryEntityInstance();

            Object value1 = getExpressionValue(context, arg1, resultProp, entityInstance);
            Object value2 = getExpressionValue(context, arg2, resultProp, entityInstance);
            EntityProperty entityProperty = entityInstance.getProperiesMap().get(resultProp);
            Object resValue = null;

            if (calculationType.equals("multiply")) { //multiply between 2 numbers
                try {
                    if (entityProperty.getType() == Type.DECIMAL)
                        resValue = (int) value1 * (int) value2;
                    else
                        resValue = (float) value1 * (float) value2;

                } catch (Exception e) {
                    throw new Exception("error while trying to do multiply for property" + entityProperty);
                }
                if (entityProperty.isExistRange()) {
                    if (entityProperty.getType() == Type.DECIMAL) {
                        if ((int) resValue >= entityProperty.getFromRange() && (int) resValue <= entityProperty.getToRange()) {

                            if ((int) resValue != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                                entityProperty.setEndTickAndAddNewElementForNextOne(context.getCurrTick(), resValue);
                            } else {
                                entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                            }
                            entityProperty.setValue(resValue);

                        } else { //stay with the same value as before
                            entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                        }
                    }
                        else if(entityProperty.getType() == Type.FLOAT) {
                                if ((float) resValue >= entityProperty.getFromRange() && (float) resValue <= entityProperty.getToRange()) {

                                    if((float) resValue != (float) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                                        entityProperty.setEndTickAndAddNewElementForNextOne(context.getCurrTick(), resValue);
                                    }  else {
                                        entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                                    }
                                    entityProperty.setValue(resValue);
                                }
                                else {
                                    entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                                }
                        }
                }
               entityInstance.getProperiesMap().put(resultProp, entityProperty);

            } else if (calculationType.equals("divide")) { // divide 2 numbers

                try {
                    if (entityProperty.getType() == Type.DECIMAL) {
                        if ((int)value2 == 0) {
                            throw new Exception("can not divide in 0 for property: " + entityProperty);
                        } else {
                            resValue = (int) value1 / (int) value2;
                        }
                    } else if (entityProperty.getType() == Type.FLOAT) {
                        if ((float)value2 == 0) {
                            throw new Exception("can not divide in 0 for property: " + entityProperty);
                        } else {
                            resValue = (float) value1 / (float) value2;
                        }
                    }
                   /* if (entityProperty.getType() == Type.DECIMAL)
                        resValue = (int) value1 / (int) value2;
                    else
                        resValue = (float) value1 / (float) value2;*/
                } catch (Exception e) {
                    throw new Exception("error while trying to do divide action for property" + entityProperty);
                }

                if (entityProperty.isExistRange()) {

                    if (entityProperty.getType() == Type.DECIMAL) {
                        if ((int) resValue >= entityProperty.getFromRange() && (int) resValue <= entityProperty.getToRange()) {
                            if ((int) resValue != (int) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                                entityProperty.setEndTickAndAddNewElementForNextOne(context.getCurrTick(), resValue);
                            }  else {
                                entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                            }
                            entityProperty.setValue(resValue);
                        } else {
                            entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                        }
                    }
                    else if(entityProperty.getType() == Type.FLOAT) {
                        if ((float) resValue >= entityProperty.getFromRange() && (float) resValue <= entityProperty.getToRange()) {

                            if((float) resValue != (float) entityProperty.getValue()) { //set in changesList of property, the tick the property changed
                                entityProperty.setEndTickAndAddNewElementForNextOne(context.getCurrTick(), resValue);
                            }  else {
                                entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                            }
                            entityProperty.setValue(resValue);
                        } else {
                            entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
                        }
                    }
                }
                entityInstance.getProperiesMap().put(resultProp, entityProperty);

            } else {
                throw new Exception("no such a calculation type: " + calculationType + "to do ");
            }
    }

    @Override
    protected Object getValue(Map <String, EntityProperty> propertyMap, String by) {
        EntityProperty entityProperty = propertyMap.get(resultProp);
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
        return "calculation";
    }

    public String getResultProp() {
        return resultProp;
    }

    public String getArg2() {
        return arg2;
    }

    public String getCalculationType() {
        return calculationType;
    }

    public String getArg1() {
        return arg1;
    }

    @Override
    public boolean existInProperty(Map<String, EntityProperty> propertyMap, String by) {
        return super.existInProperty(propertyMap, by);
    }

    public String getEntityName() {
        return entityName;
    }
}
