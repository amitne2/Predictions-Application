package dto;

import action.*;
import dto.action.ActionDTO;
import java.util.List;

public class RuleDTO {
    private String name;
    private ActivationDTO activation;
    private List<ActionDTO> actionDTOList;

    public RuleDTO(String name, ActivationDTO activationDTO, List<ActionDTO> actionsList){
        this.name = name;
        this.activation = activationDTO;
        this.actionDTOList = actionsList;
    }

    public List<ActionDTO> getActionsToPerform() { return actionDTOList; }

    public String getName() { return name; }

    public ActivationDTO getActivation() {
        return activation;
    }

}
