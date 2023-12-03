package dto.action.conditionDTO;

import action.*;
import action.condition.multipleCondition;
import action.condition.singleCondition;
import action.condition.thenAndElse.ThenElseImpl;
import dto.action.*;

import java.util.ArrayList;
import java.util.List;

public class ThenElseDTO {
    private List<ActionDTO> actionDTOList;

    public ThenElseDTO(ThenElseImpl thenElse) {
        actionDTOList = new ArrayList<>();
        for(Action action: thenElse.getActionsList()) {
            ActionDTO actionDTO = createActionDTO(action);
            actionDTOList.add(actionDTO);
        }
    }

    private ActionDTO createActionDTO(Action action) {
        ActionDTO res = null;
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
            case "kill":
                res = new KillActionDTO((KillAction) action);
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

    public int getNumOfActions() {
       return actionDTOList.size();
    }
}
