package action;
import EntityInstanceManager.EntityInstanceManagerImpl;
import action.condition.ConditionAction;
import action.condition.multipleCondition;
import action.condition.singleCondition;
import context.Context;
import entity.Type;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import grid.GridInstances;
import property.EntityProperty;
import world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static environment.helperFunctions.environment.ActiveEnvironmentImpl.converteEnvValueifNeeded;
import static environment.helperFunctions.environment.ActiveEnvironmentImpl.getEnvValue;
import static environment.helperFunctions.evaluate.ActiveEvaluateImpl.getEvaluateProperty;
import static environment.helperFunctions.percent.ActivePercentImpl.getPercent;
import static environment.helperFunctions.random.ActiveRandomImpl.random;
import static environment.helperFunctions.ticks.ActiveTicksImpl.getTicksOfChangeActive;

public abstract class Action {
    private final String actionName;
    private boolean isExistSecondary;
    private SeconderyEntity entitySecondary;


    public Action(String actionType) {
        this.actionName = actionType;
        this.isExistSecondary = false;
    }

    public Action(String actionType, SeconderyEntity entityNameSecondary) {
        this.actionName = actionType;
        if (entityNameSecondary != null) {
            this.isExistSecondary = true;
            this.entitySecondary = entityNameSecondary;
        } else
            this.isExistSecondary = false;
    }

    public SeconderyEntity getSecondaryInfo() {
        return entitySecondary;
    }

    public boolean isExistSecondary() {
        return isExistSecondary;
    }

    public List<EntityInstanceImpl> createSecondaryList(EntityInstanceManagerImpl entityInstanceManager, EnvVariableManagerImpl envVariableManager, long currTick
                    , World world, GridInstances gridInstances) throws Exception {
        List<EntityInstanceImpl> entitySecondaryInstancesList = new ArrayList<>();
        List<EntityInstanceImpl> randomEntitiesSeconderyList = new ArrayList<>();
        String seconderyEntityName = getSecondaryInfo().getEntityName();

        for(EntityInstanceImpl entityInstance: entityInstanceManager.getInstancesListByName(seconderyEntityName)) {
            Context context = new Context(entityInstance, envVariableManager, entityInstanceManager, currTick, world, gridInstances);
            entitySecondary.getConditionAction().doAction(context);
            if(entitySecondary.getConditionAction().getRes())
                 entitySecondaryInstancesList.add(entityInstance);
        }
        if(entitySecondaryInstancesList.size() == 0)
            return null;
        else {
        if (entitySecondary.getCount() == entitySecondary.ALL) //takes all valid instances
            return entitySecondaryInstancesList;

     else if (entitySecondary.getCount() > entitySecondaryInstancesList.size()) { //random list size instances
            Random random = new Random();
            for(int i=0;i<entitySecondaryInstancesList.size();i++) { //random as list size elements from list
                int randomNumber = random.nextInt(entitySecondaryInstancesList.size()); //random number between 0 to size-1
                randomEntitiesSeconderyList.add(entitySecondaryInstancesList.get(randomNumber));
            }
            return randomEntitiesSeconderyList;

        } else { //random count instances
         for(int i=0;i< entitySecondary.getCount();i++) {
             Random random = new Random();
             int randomNumber = random.nextInt(entitySecondaryInstancesList.size()); //random number between 0 to size-1
             randomEntitiesSeconderyList.add(entitySecondaryInstancesList.get(randomNumber));
            }
                return randomEntitiesSeconderyList;
            }
        }
    }

    public abstract void doAction(Context context) throws Exception;

    protected abstract Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception;

    public abstract String getActionName();


    public boolean existInProperty(Map<String, EntityProperty> propertyMap, String by) {
        for (EntityProperty property : propertyMap.values()) {
            if (by.equals(property.getName())) {
                return true;
            }
        }
        return false;
    }

    public Object getExpressionValue(Context context, String by, String propertyName, EntityInstanceImpl entityInstance) throws Exception {
        Object resValue = null;
        int startIndex, endIndex;
        String content;

        //random function
        if (by.startsWith("random")) {
            startIndex = by.indexOf("(") + 1;
            endIndex = by.indexOf(")");
            String arg = by.substring(startIndex, endIndex);
            try {
                int argRandom = Integer.parseInt(arg);
                resValue = random(argRandom);
                if(propertyName !=null){
                    if(entityInstance.getTypeOfPropertyByName(propertyName) == Type.FLOAT) {
                        float res = (float) ((Integer)resValue);
                        resValue = res;
                    }
                }
            } catch (Exception e) {
                throw new Exception("error while trying to random int number between 0 to:" + arg);
            }

            //environment function
        } else if (by.startsWith("environment")) {
            startIndex = by.indexOf("(") + 1;
            endIndex = by.indexOf(")");
            String arg = by.substring(startIndex, endIndex);
            try {
                resValue = getEnvValue(context.getEnvVariableManager(), arg);

                //convert if needed
                if(propertyName !=null) {
                    Type propertyType = entityInstance.getProperiesMap().get(propertyName).getType();
                    if ((resValue instanceof Integer) && propertyType == Type.FLOAT) {
                        int resInt = (int) resValue;
                        float resFloat = (float) resInt;
                        resValue = resFloat;
                    } else if (resValue instanceof String && propertyType == Type.BOOLEAN) {
                        if (((String) resValue).equalsIgnoreCase("true")) {
                            boolean resTrue = true;
                            resValue = resTrue;
                        } else if (((String) resValue).equalsIgnoreCase("false")) {
                            boolean resFalse = false;
                            resValue = resFalse;
                        }
                    }
                }
            } catch (Exception e) {
                throw new Exception("An error was happened during function environment");
            }
            //percent function
        } else if (by.startsWith("percent")) {
            // Find the index of the opening and closing parentheses of "percent()"
            startIndex = by.indexOf("percent(");
            endIndex = by.indexOf(")");
            String[] arguments = new String[2];
            if (startIndex != -1 && endIndex != -1) {
                // Extract the content between the parentheses of "percent()"
                arguments[0] = by.substring(startIndex + "percent(".length(), endIndex+1);
                arguments[1] = by.substring(endIndex+2, by.length()-1);

                    String arg1 = arguments[0].trim();
                    String arg2 = arguments[1].trim();
                    Object resArg1 = getExpressionValue(context, arg1, propertyName, entityInstance);
                    Object resArg2 = getExpressionValue(context, arg2, propertyName, entityInstance);
                    resValue = getPercent(resArg1, resArg2);

                } else
                    throw new Exception("The format for percent function is percent(byExp1, byExp2). The expression was: " + by);

            //evaluate function
            } else if (by.startsWith("evaluate")) {
                    // Find the index of the opening and closing parentheses
                    startIndex = by.indexOf('(');
                    endIndex = by.indexOf(')');

                    // Check if both parentheses are found
                    if (startIndex != -1 && endIndex != -1) {
                        // Extract the content between the parentheses
                        content = by.substring(startIndex + 1, endIndex);

                        // Split the content by the dot to get "ent-1" and "p1"
                        String[] parts = content.split("\\.");

                        if (parts.length != 2)
                            throw new Exception("By exp in evaluate should be   evaluate(entityName.propertyName). The by was: " + by);
                        else {
                            String entity = parts[0];
                            String property = parts[1];
                            resValue = getEvaluateProperty(context, entity, property);
                        }
                    } else
                        throw new Exception("By exp in evaluate should be evaluate(entityName.propertyName). The by was: " + by);

                    //ticks function
                } else if (by.startsWith("ticks")) {
                        startIndex = by.indexOf('(');
                        endIndex = by.indexOf(')');

                    // Check if both parentheses are found
                    if (startIndex != -1 && endIndex != -1) {
                     // Extract the content between the parentheses
                        content = by.substring(startIndex + 1, endIndex);

                        // Split the content by the dot to get "ent-1" and "p1"
                     String[] parts = content.split("\\.");

                        if (parts.length != 2)
                            throw new Exception("By exp in evaluate should be   evaluate(entityName.propertyName). The by was: " + by);
                        else {
                            String entity = parts[0];
                            String property = parts[1];
                        resValue = getTicksOfChangeActive(context, entity, property);
                    }
                } else
                    throw new Exception("By exp in evaluate should be evaluate(entityName.propertyName). The by was: " + by);


                } else if (existInProperty(context.getPrimaryEntityInstance().getProperiesMap(), by)) { //property name
                    EntityProperty entityProperty = context.getPrimaryEntityInstance().getProperiesMap().get(by);
                    resValue = entityProperty.getValue();
                } else { //free text
                    resValue = getValue(context.getPrimaryEntityInstance().getProperiesMap(), by);
                }

        return resValue;
    }

    public String getActiveName() {
        return actionName;
    }

    public String getPrimaryEntityNameOnAction(Action currAction) {
        String entityName = null;
        if (currAction.getClass() == ReplaceAction.class) { //get the kill entity name
            entityName = ((ReplaceAction) currAction).getKillEntityName();
        } else if (currAction.getClass() == ProximityAction.class) {  //get the source entity name
            entityName = ((ProximityAction) currAction).getSourceEntity();
        } else if (currAction.getClass() == IncreaseAction.class) {
            entityName = ((IncreaseAction) currAction).getEntityName();
        } else if (currAction.getClass() == DecreaseAction.class) {
            entityName = ((DecreaseAction) currAction).getEntityName();
        } else if (currAction.getClass() == CalculationAction.class) {
            entityName = ((CalculationAction) currAction).getEntityName();
        } else if (currAction.getClass() == SetAction.class) {
            entityName = ((SetAction) currAction).getEntityName();
        } else if (currAction.getClass() == KillAction.class) {
            entityName = ((KillAction) currAction).getEntityName();
        } else if (currAction.getClass() == ConditionAction.class) {
            entityName = ((ConditionAction) currAction).getEntityName();
        }else if(currAction.getClass() == multipleCondition.class) {
            entityName = ((multipleCondition) currAction).getEntityName();
        } else if(currAction.getClass() == singleCondition.class) {
            entityName = ((singleCondition) currAction).getEntityName();
        }
        return entityName;
    }

    public abstract String getEntityName();
}
