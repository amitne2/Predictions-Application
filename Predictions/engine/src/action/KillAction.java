package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import property.EntityProperty;

import java.util.Map;


public class KillAction extends Action{
    private String entityName;
    public KillAction(String entityName, String actionType) throws Exception {
        super(actionType);
        this.entityName = entityName;
    }

    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance;
        if(context.getPrimaryEntityInstance().getName().equals(entityName))
            entityInstance = context.getPrimaryEntityInstance();
        else
            entityInstance = context.getSecondaryEntityInstance();

        entityInstance.KillInstance();
    }

    @Override
    protected Object getValue(Map<String, EntityProperty> propertyMap, String by) {
        return null;
    }

    @Override
    public String getActionName() {
        return "kill";
    }

    public String getEntityName() {
        return entityName;
    }
}
