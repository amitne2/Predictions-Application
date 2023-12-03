package action;

import action.condition.ConditionAction;

public class SeconderyEntity {
    final int ALL = -1;

    private int count;
    private String entityName;
    private ConditionAction conditionAction;
    private boolean res;

    public SeconderyEntity(int count, String entityName, ConditionAction conditionAction) {
        this.count = count;
        this.entityName = entityName;
        this.conditionAction = conditionAction;
    }

    public String getEntityName() {
        return entityName;
    }

    public ConditionAction getConditionAction() {
        return conditionAction;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean getRes(){
        return conditionAction.getRes();
    }
}
