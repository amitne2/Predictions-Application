package dto.action.conditionDTO;

import action.condition.singleCondition;

public class SingleConditionDTO extends ConditionDTO {
    private String propertyName;
    private String operator;
    private String value;

    private boolean res;

    public SingleConditionDTO(singleCondition condition){
        super(condition);
        this.propertyName = condition.getPropertyName();
        this.operator = condition.getOperator();
        this.value = condition.getValue();
        this.res = condition.getRes();
    }

    public String getPropertyName() {
        return propertyName;
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    public boolean isRes() {
        return res;
    }

    public String getValue() {
        return value;
    }

    public String getOperator() {
        return operator;
    }

}
