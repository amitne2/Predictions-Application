package action.condition;

import action.Action;
import action.SeconderyEntity;
import action.condition.thenAndElse.ThenElseImpl;
import context.Context;
import property.EntityProperty;

import java.util.Map;

public abstract class ConditionAction extends Action {
    private String entityName;

    private  String singularity;
    private ThenElseImpl thenOp;
    private ThenElseImpl elseOp;

    public ThenElseImpl getThenOp(){
        return thenOp;
    }
    public ThenElseImpl getElseOp(){
        return elseOp;
    }

    public ConditionAction(String givenEntityName,String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp)  {
        super(actionType);
        this.entityName = givenEntityName;
        this.thenOp = thenOp;
        this.elseOp = elseOp;
    }

    public ConditionAction(String givenEntityName, String actionType, ThenElseImpl thenOp, ThenElseImpl elseOp, SeconderyEntity seconderyEntity)  {
        super(actionType, seconderyEntity);
        this.entityName = givenEntityName;
        this.thenOp = thenOp;
        this.elseOp = elseOp;
    }


    @Override
    public abstract void doAction(Context context) throws Exception ;

    @Override
    protected abstract Object getValue(Map<String, EntityProperty> propertyMap, String by) throws Exception;

    @Override
    public String getActionName() {
        return getActiveName();
    }

    public abstract void setRes(boolean res);

    public abstract boolean getRes();

    public String getEntityName() {
        return entityName;
    }
}
