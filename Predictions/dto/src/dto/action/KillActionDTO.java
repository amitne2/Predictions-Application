package dto.action;

import action.KillAction;
import dto.action.ActionDTO;

public class KillActionDTO extends ActionDTO {
    private String entityName;

    public KillActionDTO(KillAction killAction) {
        super(killAction);
        this.entityName = killAction.getEntityName();
    }

    public String getEntityName() {
        return entityName;
    }
}
