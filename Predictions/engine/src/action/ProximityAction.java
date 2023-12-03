package action;

import context.Context;
import entity.instance.EntityInstanceImpl;
import grid.Position;
import property.EntityProperty;

import java.util.List;
import java.util.Map;

public class ProximityAction extends Action{

    private String sourceEntity;
    private String targetEntity;
    private String envDepthOf;

    private List<Action> actionList;

    public ProximityAction(String actionType, String sourceEntity, String targetEntity, String envDepthOf, List<Action> actionList) {
        super(actionType);
        this.sourceEntity = sourceEntity;
        this.targetEntity= targetEntity;
        this.envDepthOf = envDepthOf;
        this.actionList = actionList;
    }
    public ProximityAction(String actionType, String sourceEntity, String targetEntity, String envDepthOf, List<Action> actionList, SeconderyEntity seconderyEntity) {
        super(actionType, seconderyEntity);
        this.sourceEntity = sourceEntity;
        this.targetEntity= targetEntity;
        this.envDepthOf = envDepthOf;
        this.actionList = actionList;
    }
    @Override
    public void doAction(Context context) throws Exception {
        EntityInstanceImpl entityInstance;
        if(context.getPrimaryEntityInstance().getName().equals(sourceEntity))
            entityInstance = context.getPrimaryEntityInstance();
        else
            entityInstance = context.getSecondaryEntityInstance();

        int proximityCircleValue = 0;
        Position source = entityInstance.getPositionOnGrid();
        Object proximityCircle = checkByExpType(context, envDepthOf, entityInstance);

        try {
            if(proximityCircle instanceof String)
                proximityCircleValue = Integer.parseInt((String) proximityCircle);
            else if (proximityCircle instanceof Float)
                proximityCircleValue = (int) ((float)proximityCircle);
            else if(proximityCircle instanceof Integer)
                proximityCircleValue = (int) proximityCircle;
            else if (proximityCircle instanceof Boolean)
                throw new Exception("envDepth need to be number but got boolean value" + proximityCircle);

        } catch (Exception e) { //proximity circle is from type string/boolean
            throw new Exception("envDepth of value need to expression  that his result should be a decimal value but got " + proximityCircle);
        }
        EntityInstanceImpl targetInstance = checkPointProximity(source, proximityCircleValue, context);
        if(targetInstance != null) {
            context.setSecondaryEntityInstance(targetInstance);
            for(Action currAction: actionList) {
                currAction.doAction(context);
            }
        }
    }

    private EntityInstanceImpl checkPointProximity(Position source, int proximityCircle, Context context) {
        int maxX = context.getGridInstances().getRows();
        int maxY = context.getGridInstances().getCols();

        //go over all circles until envDepthOf max circle
        for (int radius = 1; radius <= proximityCircle; radius++) {
            for (int dx = -radius; dx <= radius; dx++) {
                for (int dy = -radius; dy <= radius; dy++) {
                    if (dx * dx + dy * dy <= radius * radius) {
                        /*int nx = x + dx;
                        int ny = y + dy;*/
                        int nx = (source.getRowLocation() % maxX) + dx;
                        int ny = source.getColLocation() % maxY + dy;

                        // Check if the coordinates are within the grid boundaries
                        if (nx >= 0 && nx < maxX && ny >= 0 && ny < maxY) {
                            EntityInstanceImpl entityInstanceRes = context.getGridInstances().getInstanceOnPosition(nx, ny);
                            if (entityInstanceRes != null && entityInstanceRes.getName().equalsIgnoreCase(targetEntity)) {
                                return entityInstanceRes;
                            }

                        }
                    }
                }
            }
        }
        return null;
    }


    private Object checkByExpType(Context context, String byExp, EntityInstanceImpl entityInstance) throws Exception {
        Object res;
        if(isHelperFunction(byExp)) //helper function
            res = getExpressionValue(context, byExp, null, entityInstance);

        else if(existInProperty(context.getPrimaryEntityInstance().getProperiesMap(), byExp)) //property
            res = context.getPrimaryEntityInstance().getProperiesMap().get(byExp).getValue();
        else
            try {
                res = byExp; //free text
            } catch (Exception e) {
                throw new Exception("The env depth need to be exp that return a number(decimal/float).");
            }

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

    @Override
    protected Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception {
        return null;
    }

    @Override
    public String getActionName() {
        return "proximity";
    }

    @Override
    public String getEntityName() {
        return sourceEntity;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public String getEnvDepthOf() {
        return envDepthOf;
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public List<Action> getActionList() {
        return actionList;
    }
}
