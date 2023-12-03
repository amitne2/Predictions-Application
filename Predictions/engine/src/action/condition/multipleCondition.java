package action.condition;

import action.Action;
import action.SeconderyEntity;
import action.condition.thenAndElse.ThenElseImpl;
import context.Context;
import entity.Type;
import entity.instance.EntityInstanceImpl;
import environment.EnvVariableManagerImpl;
import exception.BooleanCanNotBeHigherOrLowerException;
import exception.VariableDoesNotExistsException;
import property.EntityProperty;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;

public class multipleCondition extends  ConditionAction {
    private List<ConditionAction> conditionActionList;
    private String logical;
    private boolean res;

    public multipleCondition(String entityName,String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp, String logical) throws BooleanCanNotBeHigherOrLowerException, VariableDoesNotExistsException {
        super(entityName,actionType, thenOp, elseOp);
        this.logical = logical;
        conditionActionList = new ArrayList<>();
        res = true;
    }
    public multipleCondition(String entityName, String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp, String logical, SeconderyEntity seconderyEntity) throws BooleanCanNotBeHigherOrLowerException, VariableDoesNotExistsException {
        super(entityName,actionType, thenOp, elseOp, seconderyEntity);
        this.logical = logical;
        conditionActionList = new ArrayList<>();
        res = true;
    }

    public void setRes(boolean res) {
        this.res = res;
    }


    public boolean getRes() {
        return res;
    }

    @Override
    public void doAction(Context context) throws Exception {
        boolean resPrev = true;
        boolean resCurr = true;

        Iterator<ConditionAction> iterator = conditionActionList.iterator();
        ConditionAction currentCondition = iterator.next();

        //first condition, at least 2 conditions in multiple list of conditions
        resPrev = checkIfTheActionRelatedToTheEntities(currentCondition, context);

        //currentCondition.doAction(context);
       // resPrev = currentCondition.getRes();
        //currentCondition = iterator.next();

        do {
            currentCondition = iterator.next();
            resCurr = checkIfTheActionRelatedToTheEntities(currentCondition, context);
            //currentCondition.doAction(context);
            //resCurr = currentCondition.getRes();

            if (logical.equals("or")) {
                if (!resPrev && !resCurr)
                    res = false;
            }
            else if (logical.equals("and")) {
                    if (!(resPrev && resCurr))
                        res = false;
            }
            resPrev = resCurr;
            /*if (iterator.hasNext())*/
            //currentCondition = iterator.next();
       // } while (res && iterator.hasNext());

        } while (iterator.hasNext());
        if(res) {
            for (Action action : getThenOp().getActionsList()) {
                action.doAction(context);
            }
            res = true;
        }
        else { // condition is false
            if (!getElseOp().equals(null)) {
                for (Action action : getElseOp().getActionsList()) {
                    action.doAction(context);
                }
            }
            res = true;
        }
        //else do nothing
    }

    private boolean checkIfTheActionRelatedToTheEntities(ConditionAction currentCondition, Context context) throws Exception {
        EntityInstanceImpl entityInstance = null;
        boolean res=false;
        boolean found = false;
        if(context.getPrimaryEntityInstance().getName().equals(currentCondition.getEntityName())) {
            currentCondition.doAction(context);
            found = true;
            res =  currentCondition.getRes();
        }
        else if(context.getSecondaryEntityInstance() != null) {
            if (context.getSecondaryEntityInstance().getName().equals(currentCondition.getEntityName())) {
                currentCondition.doAction(context);
                found = true;
                res = currentCondition.getRes();
            }
        }
        else { //the entity name in this action not the primary/secondary
            if (logical.equals("or")) { //return false to not reference to this condition in the logical
                res = false;
                found = true;
            }
            if (logical.equals("and")) {
                found = true;
                res = true;
            }

            if(!found)
                throw new Exception("En error in action condition");
        }
        return res;
    }

    @Override
    protected Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception{
        return by;
    }
    public void addCondition(ConditionAction condition){
        conditionActionList.add(condition);
    }

    public List<ConditionAction> getConditionActionList() {
        return conditionActionList;
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    @Override
    public String getEntityName() {
        return super.getEntityName();
    }

    public String getLogical() {
        return logical;
    }

    @Override
    public ThenElseImpl getElseOp() {
        return super.getElseOp();
    }

    @Override
    public ThenElseImpl getThenOp() {
        return super.getThenOp();
    }
}
