package action.condition;

import action.Action;
import action.SeconderyEntity;
import action.condition.thenAndElse.ThenElseImpl;
import context.Context;
import entity.Type;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;

public class singleCondition extends  ConditionAction {

    private String propertyName;
    private String operator;
    private String value;

    private boolean res;

    public singleCondition(String entityName, String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp, String propertyName, String operator, String value) {
        super(entityName, actionType, thenOp, elseOp);
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }

    public singleCondition(String entityName, String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp, String propertyName, String operator, String value, SeconderyEntity seconderyEntity) {

        super(entityName, actionType, thenOp, elseOp, seconderyEntity);
        this.propertyName = propertyName;
        this.operator = operator;
        this.value = value;
    }

    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance = null;
        if(context.getPrimaryEntityInstance().getName().equals(getEntityName()))
            entityInstance = context.getPrimaryEntityInstance();
        else if(context.getSecondaryEntityInstance() != null) {
            if (context.getSecondaryEntityInstance().getName().equals(getEntityName()))
                entityInstance = context.getSecondaryEntityInstance();
        }
        else //the entity name in this action not the primary/secondary
            return;

        Object checkedPropertyVal, checkedConditionValue;
        checkedPropertyVal = checkByExpType(context, propertyName,entityInstance);
        checkedConditionValue = checkByExpType(context, value, entityInstance);
        checkedConditionValue = checkSameTypeAsPropertyVal(checkedPropertyVal, checkedConditionValue); //need to check!!

        res = operatorResult(checkedConditionValue, checkedPropertyVal);

        if(res){ //the condition is true
            if(getThenOp() != null) {
                for (Action action : getThenOp().getActionsList()) {
                    action.doAction(context);
                }
            }
        } else { //the condition false, going to else
            if(getElseOp()!=null) {
                for (Action action : getElseOp().getActionsList()) {
                    action.doAction(context);
                }
            }
        }
    }

    private Object checkSameTypeAsPropertyVal(Object checkedPropertyVal, Object checkedConditionValue) throws Exception {
        Object res = checkedConditionValue ;
        if(checkedPropertyVal instanceof Long) { //came from ticks function
            try {
                res = Long.parseLong((String) checkedConditionValue);
            }  catch (Exception e) {
            throw new Exception("Property value" + checkedPropertyVal + " not match the condition value: " + checkedConditionValue);
            }
        }
        else if(checkedPropertyVal instanceof Integer) {
            try {
                res = Integer.parseInt((String) checkedConditionValue);
            } catch (Exception e) {
                throw new Exception("Property value" + checkedPropertyVal + " not match the condition value: " + checkedConditionValue);
            }
        } else if(checkedPropertyVal instanceof Float) {
            try {
                res = Float.parseFloat((String) checkedConditionValue);
            } catch (Exception e) {
                    throw new Exception("Property value" + checkedPropertyVal + " (from type float) not match the condition value: " + checkedConditionValue);
                }
            } else if(checkedPropertyVal instanceof Boolean) {
            try {
                if(checkedConditionValue instanceof Boolean)
                    res = checkedConditionValue;
                else
                res = Boolean.parseBoolean((String) checkedConditionValue);
            } catch (Exception e) {
                throw new Exception("Property value" + checkedPropertyVal + " (from type boolean) not match the condition value: " + checkedConditionValue);
            }
        } //else from type string
        return res;
    }

    private Object checkByExpType(Context context, String byExp, EntityInstanceImpl entityInstance) throws Exception {
        Object res;
        if(isHelperFunction(byExp)) //helper function
            res = getExpressionValue(context, byExp, null, entityInstance);

        else if(existInProperty(context.getPrimaryEntityInstance().getProperiesMap(), byExp)) //property
            res = entityInstance.getProperiesMap().get(byExp).getValue();
        else
            res = byExp; //free text

        return res;
    }

    private boolean isHelperFunction(String propertyName) {
        if(propertyName.startsWith("random") || propertyName.startsWith("environment") ||
                propertyName.startsWith("evaluate") || propertyName.startsWith("ticks") || propertyName.startsWith("percent")) {
            return true;
        }
        else
            return false;
    }

    public boolean operatorResult(Object conditionVal, Object propertyVal) throws Exception {
        boolean res = false;

        switch (operator) {
            case "=":
                res = propertyVal.equals(conditionVal);
                break;
            case "!=":
                res = !(propertyVal.equals(conditionVal));
                break;
            case "bt":
                if(propertyVal instanceof Integer)
                    res = (int)propertyVal > (int)conditionVal;
                else if(propertyVal instanceof Float)
                    res = (float) propertyVal > (float) conditionVal;
                else if (propertyVal instanceof String) {
                    int compareStr = (((String) propertyVal).compareTo((String)conditionVal));
                    res = compareStr > 0;
                } else if(propertyVal instanceof Boolean) {
                    throw new Exception("Can not check if one boolean value is bigger than the other.");
                } else if(propertyVal instanceof Long) {
                    res = (long)propertyVal > (long)conditionVal;
                }
                break;
            case "lt":
                if(propertyVal instanceof Integer)
                    res = (int)propertyVal < (int)conditionVal;
                else if(propertyVal instanceof Float)
                    res = (float)propertyVal < (float) conditionVal;
                else if(propertyVal instanceof String) {
                    int compareStr = (((String) propertyVal).compareTo((String)conditionVal));
                    res = compareStr < 0;
                } else if(propertyVal instanceof Boolean)
                    throw new Exception("Can not check if one boolean value is smaller than the other.");

                break;
        }
        return res;
    }

    @Override
    protected Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception {

        EntityProperty entityProperty = propertyMap.get(propertyName);
        if(entityProperty.getType() == Type.DECIMAL)
            return Integer.parseInt(by);
         else if(entityProperty.getType() == Type.FLOAT)
            return Float.parseFloat(by);
         else if (entityProperty.getType() == Type.BOOLEAN)
             return Boolean.parseBoolean(by);
         else
             return by;
    }

    public void setRes(boolean res){
        this.res = res;
    }

    public boolean getRes() {
        return res;
    }

    @Override
    public ThenElseImpl getThenOp() {
        return super.getThenOp();
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public ThenElseImpl getElseOp() {
        return super.getElseOp();
    }

    @Override
    public String getEntityName() {
        return super.getEntityName();
    }

    public String getOperator() {
        return operator;
    }

    public String getValue()
    { return value; }
}
