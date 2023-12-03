package dto.action;

import action.*;
import action.condition.multipleCondition;
import action.condition.singleCondition;
import dto.action.conditionDTO.MultipleConditionActionDTO;
import dto.action.conditionDTO.SingleConditionDTO;

import java.util.ArrayList;
import java.util.List;

public class ProximityActionDTO extends ActionDTO {

    private String sourceEntity;
    private String targetEntity;
    private String envDepthOf;

    private List<ActionDTO> actionList;

    public ProximityActionDTO(ProximityAction proximityAction) {
        super(proximityAction);
        this.actionList = new ArrayList<>();
        this.sourceEntity = proximityAction.getSourceEntity();
        this.targetEntity = proximityAction.getTargetEntity();
        this.envDepthOf = proximityAction.getEnvDepthOf();
        for(Action action: proximityAction.getActionList()) {
            ActionDTO actionDTO = createActionDTO(action);
            this.actionList.add(actionDTO);
        }
    }

    private ActionDTO createActionDTO(Action action) {
        ActionDTO res =null;
        String actionName = action.getActionName();
        switch (actionName) {
            case "increase":
                res = new IncreaseActionDTO((IncreaseAction) action);
                break;
            case "decrease":
                res = new DecreaseActionDTO((DecreaseAction) action);
                break;
            case "calculation":
                res = new CalculationActionDTO((CalculationAction) action);
                break;
            case "set":
                res = new SetActionDTO((SetAction) action);
                break;
            case "replace":
                res = new ReplaceActionDTO((ReplaceAction) action);
                break;
            case "proximity":
                res = new ProximityActionDTO((ProximityAction) action);
                break;
            case "condition":
                if(action.getClass() == multipleCondition.class)
                    res = new MultipleConditionActionDTO((multipleCondition) action);
                else if(action.getClass() == singleCondition.class)
                    res = new SingleConditionDTO((singleCondition) action);
                break;
        }
        return res;
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    public String getTargetEntity() {
        return targetEntity;
    }

    public String getEnvDepthOf() {
        return envDepthOf;
    }

    public String getSourceEntity() {
        return sourceEntity;
    }

    public int getNumOfActions() {
        return actionList.size();
    }
}
