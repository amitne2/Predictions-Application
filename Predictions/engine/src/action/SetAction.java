package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import entity.Type;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;

public class SetAction extends Action {
    private String entityName;
    private String propertyName;
    private String value;

    public SetAction(String actionType, String entityName, String propertyName, String value) throws Exception {
        super(actionType);
        this.entityName = entityName;
        this.propertyName = propertyName;
        this.value = value;
    }
    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance;
        if(context.getPrimaryEntityInstance().getName().equals(entityName))
            entityInstance = context.getPrimaryEntityInstance();
        else
            entityInstance = context.getSecondaryEntityInstance();

        Object res = getExpressionValue(context, value, propertyName, entityInstance);
        EntityProperty entityProperty = context.getPrimaryEntityInstance().getProperiesMap().get(propertyName);

        if(!(entityProperty.getValue().equals(res))) { //return to it!!
            entityProperty.setEndTickAndAddNewElementForNextOne(context.getCurrTick(), res);
        } else {
            entityProperty.setAnotherTickAsTheSameValue(context.getCurrTick());
        }
        entityProperty.setValue(res);
        entityInstance.getProperiesMap().put(propertyName, entityProperty);
    }

    @Override
    protected Object getValue(Map <String, EntityProperty> propertyMap, String by) {
        Object resValue =null;
        EntityProperty entityProperty = propertyMap.get(propertyName);
        Type type = entityProperty.getType();
        switch (type) {
            case BOOLEAN:
                resValue = Boolean.parseBoolean(value);
                break;
            case STRING:
                resValue = by;
                break;
            case DECIMAL:
                resValue = Integer.parseInt(by);
                break;
            case FLOAT:
                resValue = Float.parseFloat(by);
                break;
            default:
                break;
        }
        return resValue;
    }

    @Override
    public String getActionName() {
        return "set";
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getValue() {
        return value;
    }

    public String getEntityName() {
        return entityName;
    }
}
