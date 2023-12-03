package dto.action.conditionDTO;

import action.condition.ConditionAction;
import action.condition.multipleCondition;
import action.condition.singleCondition;

import java.util.ArrayList;
import java.util.List;

public class MultipleConditionActionDTO extends ConditionDTO {

    private List<ConditionDTO> conditionActionList;
    private String logical;
    private boolean res;

    public MultipleConditionActionDTO(multipleCondition multipleConditionAction) {
        super(multipleConditionAction);
        this.logical = multipleConditionAction.getLogical();
        this.res= multipleConditionAction.getRes();
        this.conditionActionList = new ArrayList<>();

        for(ConditionAction action: multipleConditionAction.getConditionActionList()) {
            ConditionDTO conditionDTO;
            if(action.getClass() == singleCondition.class)
                 conditionDTO = new SingleConditionDTO((singleCondition) action);
            else
                 conditionDTO = new MultipleConditionActionDTO((multipleCondition)action);

            this.conditionActionList.add(conditionDTO);
        }
    }

    public String getLogical() {
        return logical;
    }

    public int getSizeConditions() {
        return conditionActionList.size();
    }
}
