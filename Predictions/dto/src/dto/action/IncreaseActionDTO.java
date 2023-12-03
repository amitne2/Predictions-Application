package dto.action;

import action.IncreaseAction;
import dto.action.ActionDTO;

public class IncreaseActionDTO extends ActionDTO {
    private String entityName;
    private final String propertyName;
    String by;

    public IncreaseActionDTO(IncreaseAction increaseAction) {
        super(increaseAction);
        this.entityName = increaseAction.getEntityName();
        this.propertyName = increaseAction.getPropertyName();
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public String getActionName() {
        return super.getActionName();
    }

    public String getBy() {
        return by;
    }
}
